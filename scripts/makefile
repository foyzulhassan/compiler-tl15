# This is where you specify the necessary source files

# Program packages and files
#   - The packages should be the path inside your src directory. eg: package1 package2/package3
PACKAGES = com/runner com/entities com/scanner com/utils

PACKAGEPROPDEST = ./bin/com/runner
# Java compiler
JAVAC = javac
JVM = 1.7

# Directory for compiled binaries
# - trailing slash is important!
BIN = ./bin/

# Directory of source files
# - trailing slash is important!
SRC = ./src/

# Java compiler flags
JAVAFLAGS = -g -d $(BIN) -cp $(SRC)

MKDIR_P = mkdir -p

# Creating a .class file
COMPILE = $(JAVAC) $(JAVAFLAGS)

EMPTY = 

JAVA_FILES = $(subst $(SRC), $(EMPTY), $(wildcard $(SRC)*.java))

ifdef PACKAGES
PACKAGEDIRS = $(addprefix $(SRC), $(PACKAGES))
PACKAGEFILES = $(subst $(SRC), $(EMPTY), $(foreach DIR, $(PACKAGEDIRS), $(wildcard $(DIR)/*.java)))
ALL_FILES = $(PACKAGEFILES) $(JAVA_FILES)

PACKAGEPROPSRC = $(subst $(SRC), $(SRC), $(foreach DIR, $(PACKAGEDIRS), $(wildcard $(DIR)/*.properties)))

else
ALL_FILES = $(wildcard $(SRC).java)
#ALL_FILES = $(JAVA_FILES)
endif

# One of these should be the "main" class listed in Runfile
# CLASS_FILES = $(subst $(SRC), $(BIN), $(ALL_FILES:.java=.class))
CLASS_FILES = $(ALL_FILES:.java=.class)

# The first target is the one that is executed when you invoke
# "make". 

all : $(addprefix $(BIN), $(CLASS_FILES))		
	chmod 777 *.sh
	
# The line describing the action starts with <TAB>
$(BIN)%.class : $(SRC)%.java
	$(COMPILE) $<	

clean : 		
	rm -rf $(BIN)*