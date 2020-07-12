package io.pusteblume.nexthink.Q5Refactoring;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public abstract class Expression {
    private Expression() {
    }

    protected abstract Element getXml(Document document);

    public abstract int evaluate();

    public boolean serializeToXml(String path) {
        File outFile = new File(path);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outFile);
             OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream)) {
            // Create document factory
            DocumentBuilderFactory docFact = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder;
            // Build document
            docBuilder = docFact.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            // Create root node
            Element root = doc.createElement("root");
            doc.appendChild(root);

            Element xml = getXml(doc);
            // Append xml content to root node
            root.appendChild(xml);

            // set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            // create string from xml tree
            StreamResult result = new StreamResult(writer);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            writer.flush();
        } catch (ParserConfigurationException e) {
            System.out.println("Configuration error: Unable to create document builder");
            return false;
        } catch (TransformerConfigurationException e) {
            System.out.println("Configuration error: Unable to create transformer");
            return false;
        } catch (TransformerException e) {
            System.out.println("Error while transforming XML document");
            return false;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return false;
        } catch (IOException e) {
            System.out.println("IO error");
            return false;
        }
        return true;
    }

    public final static class Constant extends Expression {
        private final int value;

        Constant(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }

        @Override
        protected Element getXml(Document document) {
            Element elem = document.createElement("Value");
            elem.appendChild(document.createTextNode(String.valueOf(value)));
            return elem;
        }

        @Override
        public int evaluate() {
            return value;
        }

        public static Constant cnst(int value) {
            return new Constant(value);
        }
    }

    public final static class ArithmeticExpression extends Expression {
        private final MathOperation op;
        private final Expression left;
        private final Expression right;

        public ArithmeticExpression(Expression left, MathOperation op, Expression right) {
            if (left == null || right == null) {
                throw new IllegalArgumentException(left == null ? "left" : "right" + " is null");
            }
            this.left = left;
            this.op = op;
            this.right = right;
        }

        public int evaluate() {
            return op.calc(left.evaluate(), right.evaluate());
        }

        @Override
        public String toString() {
            return String.format("(%s %s %s)", left, op, right);
        }

        protected Element getXml(Document document) {
            Element elem = document.createElement(op.xmlTagName);
            elem.appendChild(left.getXml(document));
            elem.appendChild(right.getXml(document));
            return elem;
        }

        public static ArithmeticExpression aexp(Expression left, MathOperation op, Expression right) {
            return new ArithmeticExpression(left, op, right);
        }

        public static ArithmeticExpression aexp(int left, MathOperation op, int right) {
            return new ArithmeticExpression(Constant.cnst(left), op, Constant.cnst(right));
        }

        public static ArithmeticExpression aexp(Expression left, MathOperation op, int right) {
            return new ArithmeticExpression(left, op, Constant.cnst(right));
        }

        public static ArithmeticExpression aexp(int left, MathOperation op, Expression right) {
            return new ArithmeticExpression(Constant.cnst(left), op, right);
        }
    }
}
