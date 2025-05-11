# Dossiers
SRC_DIR = src
BIN_DIR = bin

# Recherche récursive des fichiers .java
SOURCES = $(shell find $(SRC_DIR) -name "*.java")
CLASSES = $(patsubst $(SRC_DIR)/%.java, $(BIN_DIR)/%.class, $(SOURCES))

# Classe principale (adapter si ton point d'entrée change)
MAIN_CLASS = Main

# Règle par défaut
all: $(CLASSES)

# Compilation
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	mkdir -p $(dir $@)
	javac -d $(BIN_DIR) $(SOURCES)

# Exécution
run: all
	java -cp $(BIN_DIR) $(MAIN_CLASS)

# Nettoyage
clean:
	rm -rf $(BIN_DIR)
