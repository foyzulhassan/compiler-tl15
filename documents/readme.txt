																TL15 Compiler
																=============


Module: Scanner, AST, Optimizer and Machine Code Generator(Version-3.0.0 05/03/2016)

Author: Foyzul Hassan


GENERAL USAGE GUIDE:
-------------------

1. To Run the Compiler Make Sure that JAVA is installed with proper JAVA_HOME and PATH variable value.


2. To compile and run the software run following commands in scripts folder.

		For Linux Build: 
				./build_linux.sh

		For Linux Application Execute:
				./exec_linux.sh
				or
				./exec_linux.sh sourcefile.tl
				


		For Windows Build: 
				build_win.bat
				
		For Windows Application Execute:
				exec_win.bat
				or
				exec_win.bat sourcefile.tl
				
		
		
		Note: If in Linux executing .sh files faces shows permission related error, please execute following command
		
		chmod 777 *.sh
			
3. You can provide TL15 source file as parameter or after running the program it will ask for source file path. Make sure that file extension contains "tl".


4. AST dot, CFG file and Execution File will be generated at TL15.0 source file directory with same name as source file; but extension will be .ast.dot, .cfg.dot and .s respectively.


Note: You can also import the project in Eclipse.


Thanks for using the TL15.0 Compiler.