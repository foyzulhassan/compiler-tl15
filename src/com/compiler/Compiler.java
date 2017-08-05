package com.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.ast.ParseTreeGenerator;
import com.block.Block;
import com.block.BlockGenerator;
import com.nodes.Program;
import com.optimization.Optimizer;
import com.parser.Parser;
import com.utils.OutputFileWriter;

public class Compiler {

	private ParseTreeGenerator treeGenerator;
	private BlockGenerator blockCodeGenerator;
	Map<String, Block> mBlockMap = new HashMap<String, Block>();
	private Optimizer mOptimizer;

	public Compiler() throws FileNotFoundException {

	}

	public void GenerateAST(String filename, String filepath, String filewithoutext) {
		Parser parser = null;
		try {
			parser = new Parser(filename);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		treeGenerator = new ParseTreeGenerator(parser);
		String outputtree = treeGenerator.produceParseTree();

		String ouputpath = "";

		try {
			ouputpath = OutputFileWriter.writToFile(outputtree, filepath, filewithoutext, ".ast.dot");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("AST Output file: " + ouputpath);

	}

	public void GenerateCfgMIPSCode(String filename, String filepath, String filewithoutext) {
		Program buildAST = treeGenerator.getbuildAST();

		if (buildAST != null) {

			if (buildAST.hasError()) {
				System.out.println("AST has Error(s). Please fix Syntax and Semantics Error before Code Generation");
			} else {
				blockCodeGenerator = new BlockGenerator(buildAST);
				blockCodeGenerator.generateBlocks(buildAST);

				mBlockMap = blockCodeGenerator.getBlockMap();
				mOptimizer = new Optimizer(mBlockMap);
				mOptimizer.performOptimization();

				String outputcfg = mOptimizer.getOptimizedCfg().toString();
				String outputmips = mOptimizer.getOptimizedMips().toString();

				String ouputcfgpath = "";

				try {
					ouputcfgpath = OutputFileWriter.writToFile(outputcfg, filepath, filewithoutext, ".cfg.dot");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("CFG Output file: " + ouputcfgpath);

				String ouputpathmips = "";

				try {
					ouputpathmips = OutputFileWriter.writToFile(outputmips, filepath, filewithoutext, ".s");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("MIPS Output file: " + ouputpathmips);

			}

		}

	}

	public static void main(String[] args) throws IOException {
		String filename = null;
		String filebasename = null;
		String filewithoutext = null;
		String fileextension = null;
		String filepath = null;
		boolean isparamprovided = true;		

		// If argument is not provided
		if (args.length < 1) {
			System.out.println("Please provide TL15 source file as program parameter:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			filename = br.readLine();
			isparamprovided = false;
		}

		// if argument provided the get file name from args parameter
		if (isparamprovided == true)
			filename = args[0];

		File file = new File(filename);

		// If source file path is not proper
		if (!file.exists())
			throw new RuntimeException("Source TL15.0 File No Found");

		// Get file name such as sqrt.tl from full path
		filebasename = file.getName();

		int pos = filebasename.lastIndexOf(".");

		if (pos > 0) {
			// get file name without extension
			filewithoutext = filebasename.substring(0, pos);
		}

		// get src file path as intermediate files will be stored in that
		// directory
		filepath = file.getAbsoluteFile().getParent();

		int i = filename.lastIndexOf('.');
		if (i > 0) {
			fileextension = filename.substring(i + 1);
		}

		if (!fileextension.contains("tl"))
			throw new RuntimeException("Input Source FileName does not end with .tl");

		Compiler compiler = new Compiler();
		compiler.GenerateAST(filename, filepath, filewithoutext);
		compiler.GenerateCfgMIPSCode(filename, filepath, filewithoutext);
	}
}
