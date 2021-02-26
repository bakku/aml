package dev.bakku.aml.language.parser;

import com.oracle.truffle.api.frame.FrameSlot;
import dev.bakku.aml.language.AMLBaseVisitor;
import dev.bakku.aml.language.AMLContext;
import dev.bakku.aml.language.AMLLexer;
import dev.bakku.aml.language.AMLParser;
import dev.bakku.aml.language.nodes.*;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.beans.Expression;
import java.util.List;

public class AMLAntlrVisitor extends AMLBaseVisitor<AMLBaseNode> {
    private final AMLContext context;

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
                if (c instanceof AMLParser.LibraryContext) {
                    return this.visitLibrary((AMLParser.LibraryContext) c);
                } else if (c instanceof AMLParser.FunctionContext) {
                    return this.visitFunction((AMLParser.FunctionContext) c);
                } else {
                    return this.visitExpression((AMLParser.ExpressionContext) c);
                }
            }).toArray(AMLBaseNode[]::new);

        return new AMLProgramNode(nodes);
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
            expressionsToProgram(ctx.thenBranch().expression()),
            expressionsToProgram(ctx.elseBranch().expression())
        );
    }

    @Override
    public AMLBaseNode visitAssignment(AMLParser.AssignmentContext ctx) {
        return this.visitLogicEquivalence(ctx.logicEquivalence());
    }

    @Override
    public AMLBaseNode visitLogicEquivalence(AMLParser.LogicEquivalenceContext ctx) {
        return this.visitLogicImplication(ctx.logicImplication(0));
    }

    @Override
    public AMLBaseNode visitLogicImplication(AMLParser.LogicImplicationContext ctx) {
        return this.visitLogicOr(ctx.logicOr(0));
    }

    @Override
    public AMLBaseNode visitLogicOr(AMLParser.LogicOrContext ctx) {
        return this.visitLogicXOr(ctx.logicXOr(0));
    }

    @Override
    public AMLBaseNode visitLogicXOr(AMLParser.LogicXOrContext ctx) {
        return this.visitLogicAnd(ctx.logicAnd(0));
    }

    @Override
    public AMLBaseNode visitLogicAnd(AMLParser.LogicAndContext ctx) {
        return this.visitEquality(ctx.equality(0));
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
        return this.visitNumComparison(ctx.numComparison());
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
        if (ctx.expression() != null) {
            return this.visitExpression(ctx.expression());
        } else if (ctx.IDENTIFIER() != null) {
            return AMLReadValueNodeGen.create(
                this.context.getGlobalFrame()
                    .getFrameDescriptor()
                    .findOrAddFrameSlot(ctx.IDENTIFIER().getSymbol().getText())
            );
        }

        return this.visitNumber(ctx.number());
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

    private AMLProgramNode expressionsToProgram(List<AMLParser.ExpressionContext> expressions) {
        return new AMLProgramNode(
            expressions.stream()
                .map(this::visitExpression)
                .toArray(AMLBaseNode[]::new)
        );
    }
}
