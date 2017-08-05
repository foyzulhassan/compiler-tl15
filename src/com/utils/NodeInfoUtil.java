package com.utils;

import java.util.Map;
import java.util.Set;

import com.nodes.Attribute;
import com.nodes.Declaration;
import com.nodes.Node;
import com.nodes.NodeLabelHelper;
import com.nodes.Program;
import com.nodes.StatementSequence;
import com.scanner.Token;

public class NodeInfoUtil {
	public static void setAttributes(Node node, Node child) {
		Map<String, Attribute> pAttributes = child.getAttributes();
		if (child.hasError()) {
			node.setError(true);
		}

		setAttributes(node, pAttributes);

	}

	public static void setAttributes(Node node, Map<String, Attribute> pAttributes) {
		if (node.getTokenValue() != null && Token.getINTOP().contains(node.getTokenValue().getWord())) {
			Attribute attribute = new Attribute();
			attribute.setValue(Token.INT);

			node.setAttribute("type", attribute);
		}

		Set<String> keySet = pAttributes.keySet();
		for (String key : keySet) {
			Attribute attribute = pAttributes.get(key);
			if (Token.getINTOP().contains(attribute.getValue())) {
				if (key.equalsIgnoreCase("type")) {
					attribute.setValue(Token.INT);

					node.setAttribute(key, attribute);

				} else
					node.setAttribute(key, attribute);
			} else
				node.setAttribute(key, attribute);
		}

	}

	public static String getTokenValue(Node pNode) {
		Token word = pNode.getTokenValue().getWord();
		if (Token.ident == word || Token.num == word || Token.boollit == word) {
			return pNode.getTokenValue().getWordValue();
		} else if (Token.COMPARE == word || Token.ADDITIVE == word || Token.MULTIPLICATIVE == word) {
			return pNode.getTokenValue().getWordValue();
		}

		return pNode.getTokenValue().getWord().getValue();
	}

	public static void buildASTview(int fCounter, int cCounter, StringBuffer pBuffer, String program, String color) {

		pBuffer.append("\n");
		String label = NodeLabelHelper.Label.replace("#", "" + cCounter);
		label = label.replace("@", program);
		label = label.replace("$", color);
		if (!program.equalsIgnoreCase("program")) {
			label = label.replace("box", "none");
		}
		pBuffer.append(label);
		pBuffer.append("\n");
		if (!program.equalsIgnoreCase("program")) {
			pBuffer.append("n" + fCounter + " -> " + "n" + cCounter);
			pBuffer.append("\n");
		}
	}

	public static Node getValidASTParentNode(Node node) {
		Node parent = null;
		Node nodeobj = node;

		while (true) {

			if (nodeobj == null)
				break;
			else if (nodeobj.getTokenValue() != null
					&& Token.getValidASTTokens().contains(nodeobj.getTokenValue().getWord())) {
				parent = nodeobj;
				break;
			} else if (nodeobj instanceof Program) {
				parent = nodeobj;
				break;
			}

			else if (nodeobj instanceof Declaration) {
				parent = nodeobj;
				break;
			}

			else if (nodeobj instanceof StatementSequence) {
				parent = nodeobj;
				break;

			} else if (nodeobj instanceof Declaration) {
				parent = nodeobj;
				break;
			} else {
				nodeobj = nodeobj.getParserParent();
			}

		}

		return parent;
	}

	public static boolean isValidASTNode(Node nodeobj) {
		boolean ret = false;

		if (nodeobj == null)
			ret = false;

		else if (nodeobj.getTokenValue() != null
				&& Token.getValidASTTokens().contains(nodeobj.getTokenValue().getWord())) {
			ret = true;

		} else if (nodeobj instanceof Program) {
			ret = true;

		}

		else if (nodeobj instanceof Declaration) {
			ret = true;

		}

		else if (nodeobj instanceof StatementSequence) {
			ret = true;

		} else if (nodeobj instanceof Declaration) {
			ret = true;

		}

		return ret;
	}

}
