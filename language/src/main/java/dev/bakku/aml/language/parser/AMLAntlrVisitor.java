package dev.bakku.aml.language.parser;

import dev.bakku.aml.language.AMLBaseVisitor;
import dev.bakku.aml.language.AMLContext;
import dev.bakku.aml.language.AMLLexer;
import dev.bakku.aml.language.AMLParser;
import dev.bakku.aml.language.nodes.functions.*;
import dev.bakku.aml.language.nodes.generic.*;
import dev.bakku.aml.language.nodes.logic.*;
import dev.bakku.aml.language.nodes.numeric.*;
import dev.bakku.aml.language.nodes.sets.*;
import dev.bakku.aml.language.nodes.variables.*;
import dev.bakku.aml.language.nodes.*;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import org.antlr.v4.runtime.tree.TerminalNode;

public class AMLAntlrVisitor extends AMLBaseVisitor<AMLBaseNode> {
    private final AMLContext context;
    private boolean inNestedScope = false;

    public AMLAntlrVisitor(AMLContext context) {
        this.context = context;
    }

    @Override
    public AMLBaseNode visitProgram(AMLParser.ProgramContext ctx) {
        var nodes = ctx.children
            .stream()
            // filter all newlines
            .filter(c -> !(c instanceof TerminalNode))
            .map(c -> {
                if (c instanceof AMLParser.FunctionContext) {
                    return this.visitFunction((AMLParser.FunctionContext) c);
                } else {
                    return this.visitExpression((AMLParser.ExpressionContext) c);
                }
            }).toArray(AMLBaseNode[]::new);

        return new AMLProgramNode(nodes);
    }

    @Override
    public AMLBaseNode visitFunction(AMLParser.FunctionContext ctx) {
        var argumentNames = ctx.params()
            .IDENTIFIER()
            .stream()
            .map(i -> i.getSymbol().getText())
            .toArray(String[]::new);

        inNestedScope = true;
        var bodyNode = this.visitExpression(ctx.expression());
        inNestedScope = false;

        return new AMLDefineFunctionNode(
            ctx.IDENTIFIER().getSymbol().getText(),
            argumentNames,
            bodyNode,
            context.getGlobalFrame(),
            this.context.getGlobalFrameDescriptor()
                .findOrAddFrameSlot(ctx.IDENTIFIER().getSymbol().getText())
        );
    }

    @Override
    public AMLBaseNode visitExpression(AMLParser.ExpressionContext ctx) {
        if (ctx.ifcond() != null) {
            return this.visitIfcond(ctx.ifcond());
        }

        return this.visitAssignment(ctx.assignment());
    }

    @Override
    public AMLBaseNode visitIfcond(AMLParser.IfcondContext ctx) {
        return new AMLIfNode(
            this.visitLogicEquivalence(ctx.logicEquivalence()),
            this.visitThenBranch(ctx.thenBranch()),
            this.visitElseBranch(ctx.elseBranch())
        );
    }

    @Override
    public AMLBaseNode visitThenBranch(AMLParser.ThenBranchContext ctx) {
        if (ctx.ifcond() != null) {
            return this.visitIfcond(ctx.ifcond());
        } else if (ctx.composition() != null) {
            return this.visitComposition(ctx.composition());
        } else {
            return this.visitLogicEquivalence(ctx.logicEquivalence());
        }
    }

    @Override
    public AMLBaseNode visitElseBranch(AMLParser.ElseBranchContext ctx) {
        if (ctx.ifcond() != null) {
            return this.visitIfcond(ctx.ifcond());
        } else if (ctx.composition() != null) {
            return this.visitComposition(ctx.composition());
        } else {
            return this.visitLogicEquivalence(ctx.logicEquivalence());
        }
    }

    @Override
    public AMLBaseNode visitComposition(AMLParser.CompositionContext ctx) {
        return AMLComposeFunctionsNodeGen.create(
            AMLReadVariableNodeGen.create(this.context.getGlobalFrame(), ctx.IDENTIFIER(0).getSymbol().getText()),
            AMLReadVariableNodeGen.create(this.context.getGlobalFrame(), ctx.IDENTIFIER(1).getSymbol().getText())
        );
    }

    @Override
    public AMLBaseNode visitAssignment(AMLParser.AssignmentContext ctx) {
        if (ctx.assignment() != null) {
            if (this.inNestedScope) {
                return AMLWriteLocalVariableNodeGen.create(
                    this.visitAssignment(ctx.assignment()),
                    ctx.IDENTIFIER().getSymbol().getText()
                );
            } else {
                var frameSlot = this.context.getGlobalFrame()
                    .getFrameDescriptor()
                    .findOrAddFrameSlot(ctx.IDENTIFIER().getSymbol().getText());

                return AMLWriteGlobalVariableNodeGen.create(
                    this.visitAssignment(ctx.assignment()),
                    this.context.getGlobalFrame(),
                    frameSlot
                );
            }
        } else if (ctx.composition() != null) {
            return this.visitComposition(ctx.composition());
        } else {
            return this.visitLogicEquivalence(ctx.logicEquivalence());
        }
    }

    @Override
    public AMLBaseNode visitLogicEquivalence(AMLParser.LogicEquivalenceContext ctx) {
        if (ctx.logicImplication().size() == 1) {
            return this.visitLogicImplication(ctx.logicImplication(0));
        } else {
            AMLBaseNode ret = this.visitLogicImplication(ctx.logicImplication(0));

            for (int i = 1; i < ctx.logicImplication().size(); i++) {
                ret = AMLEquivalenceNodeGen.create(
                    ret,
                    this.visitLogicImplication(ctx.logicImplication(i))
                );
            }

            return ret;
        }
    }

    @Override
    public AMLBaseNode visitLogicImplication(AMLParser.LogicImplicationContext ctx) {
        if (ctx.logicOr().size() == 1) {
            return this.visitLogicOr(ctx.logicOr(0));
        } else {
            AMLBaseNode ret = this.visitLogicOr(ctx.logicOr(0));

            for (int i = 1; i < ctx.logicOr().size(); i++) {
                ret = AMLImplicationNodeGen.create(
                    ret,
                    this.visitLogicOr(ctx.logicOr(i))
                );
            }

            return ret;
        }
    }

    @Override
    public AMLBaseNode visitLogicOr(AMLParser.LogicOrContext ctx) {
        if (ctx.logicXOr().size() == 1) {
            return this.visitLogicXOr(ctx.logicXOr(0));
        } else {
            AMLBaseNode ret = this.visitLogicXOr(ctx.logicXOr(0));

            for (int i = 1; i < ctx.logicXOr().size(); i++) {
                ret = AMLOrNodeGen.create(
                    ret,
                    this.visitLogicXOr(ctx.logicXOr(i))
                );
            }

            return ret;
        }
    }

    @Override
    public AMLBaseNode visitLogicXOr(AMLParser.LogicXOrContext ctx) {
        if (ctx.logicAnd().size() == 1) {
            return this.visitLogicAnd(ctx.logicAnd(0));
        } else {
            AMLBaseNode ret = this.visitLogicAnd(ctx.logicAnd(0));

            for (int i = 1; i < ctx.logicAnd().size(); i++) {
                ret = AMLXOrNodeGen.create(
                    ret,
                    this.visitLogicAnd(ctx.logicAnd(i))
                );
            }

            return ret;
        }
    }

    @Override
    public AMLBaseNode visitLogicAnd(AMLParser.LogicAndContext ctx) {
        if (ctx.equality().size() == 1) {
            return this.visitEquality(ctx.equality(0));
        } else {
            AMLBaseNode ret = this.visitEquality(ctx.equality(0));

            for (int i = 1; i < ctx.equality().size(); i++) {
                ret = AMLAndNodeGen.create(
                    ret,
                    this.visitEquality(ctx.equality(i))
                );
            }

            return ret;
        }
    }

    @Override
    public AMLBaseNode visitEquality(AMLParser.EqualityContext ctx) {
        if (ctx.getChildCount() == 1) {
            return this.visitComparison(ctx.comparison(0));
        } else {
            AMLBaseNode ret = this.visitComparison(ctx.comparison(0));

            int comparisonCounter = 1;
            int childrenCounter = 1;

            while (childrenCounter < ctx.getChildCount()) {
                var op = (TerminalNode) ctx.getChild(childrenCounter);

                switch (op.getSymbol().getType()) {
                    case AMLLexer.EQ:
                        ret = AMLEqualNodeGen.create(
                            ret,
                            this.visitComparison(ctx.comparison(comparisonCounter))
                        );
                        break;
                    case AMLLexer.NEQ:
                        ret = AMLNotEqualNodeGen.create(
                            ret,
                            this.visitComparison(ctx.comparison(comparisonCounter))
                        );
                        break;
                    default:
                        throw new AMLParserException("Unsupported term operation");
                }

                comparisonCounter++;
                childrenCounter = childrenCounter + 2;
            }

            return ret;
        }
    }

    @Override
    public AMLBaseNode visitComparison(AMLParser.ComparisonContext ctx) {
        if (ctx.numComparison() != null) {
            return this.visitNumComparison(ctx.numComparison());
        } else if (ctx.setComparison() != null) {
            return this.visitSetComparison(ctx.setComparison());
        }

        return this.visitQuantification(ctx.quantification());
    }

    @Override
    public AMLBaseNode visitNumComparison(AMLParser.NumComparisonContext ctx) {
        if (ctx.getChildCount() == 1) {
            return this.visitTerm(ctx.term(0));
        } else {
            AMLBaseNode ret = this.visitTerm(ctx.term(0));

            int termCounter = 1;
            int childrenCounter = 1;

            while (childrenCounter < ctx.getChildCount()) {
                var op = (TerminalNode) ctx.getChild(childrenCounter);

                switch (op.getSymbol().getType()) {
                    case AMLLexer.LT:
                        ret = AMLLessThanNodeGen.create(
                            ret,
                            this.visitTerm(ctx.term(termCounter))
                        );
                        break;
                    case AMLLexer.GT:
                        ret = AMLGreaterThanNodeGen.create(
                            ret,
                            this.visitTerm(ctx.term(termCounter))
                        );
                        break;
                    case AMLLexer.LTE:
                        ret = AMLLessThanEqualNodeGen.create(
                            ret,
                            this.visitTerm(ctx.term(termCounter))
                        );
                        break;
                    case AMLLexer.GTE:
                        ret = AMLGreaterThanEqualNodeGen.create(
                            ret,
                            this.visitTerm(ctx.term(termCounter))
                        );
                        break;
                    default:
                        throw new AMLParserException("Unsupported term operation");
                }

                termCounter++;
                childrenCounter = childrenCounter + 2;
            }

            return ret;
        }
    }

    @Override
    public AMLBaseNode visitTerm(AMLParser.TermContext ctx) {
        if (ctx.getChildCount() == 1) {
            return this.visitFactor(ctx.factor(0));
        } else {
            AMLBaseNode ret = this.visitFactor(ctx.factor(0));

            int factorCounter = 1;
            int childrenCounter = 1;

            while (childrenCounter < ctx.getChildCount()) {
                var op = (TerminalNode) ctx.getChild(childrenCounter);

                switch (op.getSymbol().getType()) {
                    case AMLLexer.PLUS:
                        ret = AMLAddNodeGen.create(
                            ret,
                            this.visitFactor(ctx.factor(factorCounter))
                        );
                        break;
                    case AMLLexer.MINUS:
                        ret = AMLSubNodeGen.create(
                            ret,
                            this.visitFactor(ctx.factor(factorCounter))
                        );
                        break;
                    default:
                        throw new AMLParserException("Unsupported term operation");
                }

                factorCounter++;
                childrenCounter = childrenCounter + 2;
            }

            return ret;
        }
    }

    @Override
    public AMLBaseNode visitFactor(AMLParser.FactorContext ctx) {
        if (ctx.getChildCount() == 1) {
            return this.visitExponentiation(ctx.exponentiation(0));
        } else {
            AMLBaseNode ret = this.visitExponentiation(ctx.exponentiation(0));

            int exponentiationCounter = 1;
            int childrenCounter = 1;

            while (childrenCounter < ctx.getChildCount()) {
                var op = (TerminalNode) ctx.getChild(childrenCounter);

                switch (op.getSymbol().getType()) {
                    case AMLLexer.MULTIPLY:
                        ret = AMLMultiplyNodeGen.create(
                            ret,
                            this.visitExponentiation(ctx.exponentiation(exponentiationCounter))
                        );
                        break;
                    case AMLLexer.DIVIDE:
                        ret = AMLDivideNodeGen.create(
                            ret,
                            this.visitExponentiation(ctx.exponentiation(exponentiationCounter))
                        );
                        break;
                    case AMLLexer.MOD:
                        ret = AMLModuloNodeGen.create(
                            ret,
                            this.visitExponentiation(ctx.exponentiation(exponentiationCounter))
                        );
                        break;
                    default:
                        throw new AMLParserException("Unsupported term operation");
                }

                exponentiationCounter++;
                childrenCounter = childrenCounter + 2;
            }

            return ret;
        }
    }

    @Override
    public AMLBaseNode visitExponentiation(AMLParser.ExponentiationContext ctx) {
        if (ctx.getChildCount() == 1) {
            return this.visitFraction(ctx.fraction(0));
        } else {
            AMLBaseNode ret = this.visitFraction(ctx.fraction(0));

            int fractionCounter = 1;
            int childrenCounter = 1;

            while (childrenCounter < ctx.getChildCount()) {
                ret = AMLExponentiationNodeGen.create(
                    ret,
                    this.visitFraction(ctx.fraction(fractionCounter))
                );

                fractionCounter++;
                childrenCounter = childrenCounter + 2;
            }

            return ret;
        }
    }

    @Override
    public AMLBaseNode visitFraction(AMLParser.FractionContext ctx) {
        if (ctx.getChildCount() == 3) {
            return AMLFractionNodeGen.create(
                this.visitNumUnary(ctx.numUnary(0)),
                this.visitNumUnary(ctx.numUnary(1))
            );
        }

        return this.visitNumUnary(ctx.numUnary(0));
    }

    @Override
    public AMLBaseNode visitNumUnary(AMLParser.NumUnaryContext ctx) {
        if (ctx.numNegation() != null) {
            return this.visitNumNegation(ctx.numNegation());
        } else if (ctx.factorial() != null) {
            return this.visitFactorial(ctx.factorial());
        } else if (ctx.floor() != null) {
            return this.visitFloor(ctx.floor());
        } else if (ctx.ceil() != null) {
            return this.visitCeil(ctx.ceil());
        } else {
            return this.visitNumPrimary(ctx.numPrimary());
        }
    }

    @Override
    public AMLBaseNode visitNumNegation(AMLParser.NumNegationContext ctx) {
        return AMLNegationNodeGen.create(this.visitNumPrimary(ctx.numPrimary()));
    }

    @Override
    public AMLBaseNode visitFactorial(AMLParser.FactorialContext ctx) {
        return AMLFactorialNodeGen.create(this.visitNumPrimary(ctx.numPrimary()));
    }

    @Override
    public AMLBaseNode visitFloor(AMLParser.FloorContext ctx) {
        return AMLFloorNodeGen.create(this.visitNumPrimary(ctx.numPrimary()));
    }

    @Override
    public AMLBaseNode visitCeil(AMLParser.CeilContext ctx) {
        return AMLCeilNodeGen.create(this.visitNumPrimary(ctx.numPrimary()));
    }

    @Override
    public AMLBaseNode visitNumPrimary(AMLParser.NumPrimaryContext ctx) {
        if (ctx.logicEquivalence() != null) {
            return this.visitLogicEquivalence(ctx.logicEquivalence());
        } else if (ctx.IDENTIFIER() != null) {
            return AMLReadVariableNodeGen.create(
                this.context.getGlobalFrame(),
                ctx.IDENTIFIER().getSymbol().getText()
            );
        } else if (ctx.call() != null) {
            return this.visitCall(ctx.call());
        }

        return this.visitNumber(ctx.number());
    }

    @Override
    public AMLBaseNode visitCall(AMLParser.CallContext ctx) {
        return new AMLCallNode(
            this.context.getGlobalFrame(),
            ctx.IDENTIFIER().getSymbol().getText(),
            ctx.arguments()
                .logicEquivalence()
                .stream()
                .map(this::visitLogicEquivalence)
                .toArray(AMLBaseNode[]::new)
        );
    }

    @Override
    public AMLBaseNode visitNumber(AMLParser.NumberContext ctx) {
        if (ctx.getChildCount() == 3) {
            return new AMLNumberNode(
                AMLNumber.of(ctx.NUMBER(0).toString() + "." + ctx.NUMBER(1).toString())
            );
        }

        return new AMLNumberNode(
            AMLNumber.of(ctx.NUMBER(0).toString())
        );
    }

    @Override
    public AMLBaseNode visitSetComparison(AMLParser.SetComparisonContext ctx) {
        if (ctx.getChildCount() == 1) {
            return this.visitSetOperations(ctx.setOperations(0));
        } else {
            AMLBaseNode ret = this.visitSetOperations(ctx.setOperations(0));

            int setOperationsCounter = 1;
            int childrenCounter = 1;

            while (childrenCounter < ctx.getChildCount()) {
                var op = (TerminalNode) ctx.getChild(childrenCounter);

                switch (op.getSymbol().getType()) {
                    case AMLLexer.SUBSET:
                        ret = AMLSubsetNodeGen.create(
                            ret,
                            this.visitSetOperations(ctx.setOperations(setOperationsCounter))
                        );
                        break;
                    case AMLLexer.NOT_SUBSET:
                        ret = AMLNotSubsetNodeGen.create(
                            ret,
                            this.visitSetOperations(ctx.setOperations(setOperationsCounter))
                        );
                        break;
                    case AMLLexer.SUBSET_EQ:
                        ret = AMLSubsetEqNodeGen.create(
                            ret,
                            this.visitSetOperations(ctx.setOperations(setOperationsCounter))
                        );
                        break;
                    case AMLLexer.NOT_SUBSET_EQ:
                        ret = AMLNotSubsetEqNodeGen.create(
                            ret,
                            this.visitSetOperations(ctx.setOperations(setOperationsCounter))
                        );
                        break;
                    case AMLLexer.SUPERSET:
                        ret = AMLSupersetNodeGen.create(
                            ret,
                            this.visitSetOperations(ctx.setOperations(setOperationsCounter))
                        );
                        break;
                    case AMLLexer.NOT_SUPERSET:
                        ret = AMLNotSupersetNodeGen.create(
                            ret,
                            this.visitSetOperations(ctx.setOperations(setOperationsCounter))
                        );
                        break;
                    case AMLLexer.SUPERSET_EQ:
                        ret = AMLSupersetEqNodeGen.create(
                            ret,
                            this.visitSetOperations(ctx.setOperations(setOperationsCounter))
                        );
                        break;
                    case AMLLexer.NOT_SUPERSET_EQ:
                        ret = AMLNotSupersetEqNodeGen.create(
                            ret,
                            this.visitSetOperations(ctx.setOperations(setOperationsCounter))
                        );
                        break;
                    default:
                        throw new AMLParserException("Unsupported term operation");
                }

                setOperationsCounter++;
                childrenCounter = childrenCounter + 2;
            }

            return ret;
        }
    }

    @Override
    public AMLBaseNode visitSetOperations(AMLParser.SetOperationsContext ctx) {
        if (ctx.getChildCount() == 1) {
            return this.visitSetUnary(ctx.setUnary(0));
        } else {
            AMLBaseNode ret = this.visitSetUnary(ctx.setUnary(0));

            int setUnaryCounter = 1;
            int childrenCounter = 1;

            while (childrenCounter < ctx.getChildCount()) {
                var op = (TerminalNode) ctx.getChild(childrenCounter);

                switch (op.getSymbol().getType()) {
                    case AMLLexer.INTERSECTION:
                        ret = AMLIntersectionNodeGen.create(
                            ret,
                            this.visitSetUnary(ctx.setUnary(setUnaryCounter))
                        );
                        break;
                    case AMLLexer.UNION:
                        ret = AMLUnionNodeGen.create(
                            ret,
                            this.visitSetUnary(ctx.setUnary(setUnaryCounter))
                        );
                        break;
                    case AMLLexer.BACKWARD_SLASH:
                        ret = AMLDifferenceNodeGen.create(
                            ret,
                            this.visitSetUnary(ctx.setUnary(setUnaryCounter))
                        );
                        break;
                    default:
                        throw new AMLParserException("Unsupported term operation");
                }

                setUnaryCounter++;
                childrenCounter = childrenCounter + 2;
            }

            return ret;
        }
    }

    @Override
    public AMLBaseNode visitSetUnary(AMLParser.SetUnaryContext ctx) {
        if (ctx.cardinality() != null) {
            return AMLCardinalityNodeGen.create(
                this.visitSetPrimary(ctx.cardinality().setPrimary())
            );
        }

        return this.visitSetPrimary(ctx.setPrimary());
    }

    @Override
    public AMLBaseNode visitSetPrimary(AMLParser.SetPrimaryContext ctx) {
        if (ctx.call() != null) {
            return this.visitCall(ctx.call());
        } else if (ctx.setLiteral() != null) {
            return this.visitSetLiteral(ctx.setLiteral());
        } else if (ctx.setEllipsis() != null) {
            return this.visitSetEllipsis(ctx.setEllipsis());
        } else if (ctx.logicEquivalence() != null) {
            return this.visitLogicEquivalence(ctx.logicEquivalence());
        }

        return AMLReadVariableNodeGen.create(
            this.context.getGlobalFrame(),
            ctx.IDENTIFIER().getSymbol().getText()
        );
    }

    @Override
    public AMLBaseNode visitSetLiteral(AMLParser.SetLiteralContext ctx) {
        var elements = ctx.logicEquivalence()
            .stream()
            .map(this::visitLogicEquivalence)
            .toArray(AMLBaseNode[]::new);

        return new AMLSetLiteralNode(elements);
    }

    @Override
    public AMLBaseNode visitSetEllipsis(AMLParser.SetEllipsisContext ctx) {
        return AMLSetEllipsisNodeGen.create(
            this.visitNumUnary(ctx.numUnary(0)),
            this.visitNumUnary(ctx.numUnary(1))
        );
    }
}
