SOURCE=$(shell find src -name '*.java')

.PHONY: all clean

all: $(CLASSES)

clean:
	find -L . -name '*'.class -exec rm -f {} ';'

$(CLASSES): %.java
	javac $(SOURCE) -d doc

# ensure the next line is always the last line in this file.
# vi:noet



