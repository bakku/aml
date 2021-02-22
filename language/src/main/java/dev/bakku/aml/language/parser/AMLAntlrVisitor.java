package dev.bakku.aml.language.parser;

import dev.bakku.aml.language.AMLBaseVisitor;
import dev.bakku.aml.language.AMLParser;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.nodes.AMLSetLiteralNode;
import dev.bakku.aml.language.nodes.IntersectNodeGen;
import dev.bakku.aml.language.runtime.AMLSet;

public class AMLAntlrVisitor extends AMLBaseVisitor<AMLBaseNode> {
    @Override
    public AMLBaseNode visitProgram(AMLParser.ProgramContext ctx) {
        return this.visitExpression(ctx.expression(0));
    }

    @Override
    public AMLBaseNode visitExpression(AMLParser.ExpressionContext ctx) {
        return this.visitIntersection(ctx.intersection());
    }

    @Override
    public AMLBaseNode visitIntersection(AMLParser.IntersectionContext ctx) {
        var leftNumbers = ctx.set(0).NUMBER()
            .stream()
            .map(t -> t.getSymbol().getText())
            .map(Integer::parseInt)
            .toArray(Integer[]::new);

        var rightNumbers = ctx.set(1).NUMBER()
            .stream()
            .map(t -> t.getSymbol().getText())
            .map(Integer::parseInt)
            .toArray(Integer[]::new);

        var left = AMLSet.of(leftNumbers);
        var right = AMLSet.of(rightNumbers);
        return IntersectNodeGen.create(new AMLSetLiteralNode(left), new AMLSetLiteralNode(right));
    }
}
