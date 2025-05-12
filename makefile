SRC_DIR = src
BIN_DIR = bin

SOURCES = $(shell find $(SRC_DIR) -name "*.java")
CLASSES = $(patsubst $(SRC_DIR)/%.java, $(BIN_DIR)/%.class, $(SOURCES))

MAIN_CLASS = Main

all: $(CLASSES)

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	mkdir -p $(dir $@)
	javac -d $(BIN_DIR) $(SOURCES)

run: all
	java -cp $(BIN_DIR) $(MAIN_CLASS)

clean:
	rm -rf $(BIN_DIR)
