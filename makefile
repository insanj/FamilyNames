SHELL:=/bin/bash
JAVA_HOME:=/usr/lib/jvm/jdk1.8.0_131

JAVAC_CMD=/usr/lib/jvm/jdk1.8.0_131/bin/javac
JAR_CMD=/usr/lib/jvm/jdk1.8.0_131/bin/jar

FIND_JAVA_FILES:=$(shell find . -name '*.java')
GIT_TAG:=$(shell git describe --tags)

OUTPUT_NAME=familynames
SOURCE_PATH=plugin
BUILD_PATH=build
EXTERNAL_PATH=external
SERVER_PATH=server

CRAFTBUKKIT_JAR_FILENAME=craftbukkit-1.13.2.jar
JAR_DEPS_PATH=$(EXTERNAL_PATH)/$(CRAFTBUKKIT_JAR_FILENAME)
OUTPUT_VERSIONED_NAME=$(OUTPUT_NAME)-$(GIT_TAG)

.PHONY: all
all: plugin server

.PHONY: plugin
plugin:
	export JAVA_HOME=$(JAVA_HOME)
	-rm -r -f $(BUILD_PATH)
	mkdir $(BUILD_PATH) && mkdir $(BUILD_PATH)/bin
	$(JAVAC_CMD) -cp "$(JAR_DEPS_PATH)" -d $(BUILD_PATH)/bin $(FIND_JAVA_FILES)
	-cp -r $(SOURCE_PATH)/*.yml $(BUILD_PATH)/bin/
	$(JAR_CMD) -cvf $(BUILD_PATH)/$(OUTPUT_VERSIONED_NAME).jar -C $(BUILD_PATH)/bin .

.PHONY: clean
clean:
	-rm -r -f $(SERVER_PATH)
	mkdir $(SERVER_PATH) && mkdir $(SERVER_PATH)/plugins
	echo "eula=true" > $(SERVER_PATH)/eula.txt
	cp -R $(EXTERNAL_PATH)/$(CRAFTBUKKIT_JAR_FILENAME) $(SERVER_PATH)/$(CRAFTBUKKIT_JAR_FILENAME)

.PHONY: server
server:
	-rm -r $(SERVER_PATH)/plugins/$(OUTPUT_NAME)*.jar
	cp -R $(BUILD_PATH)/$(OUTPUT_VERSIONED_NAME).jar $(SERVER_PATH)/plugins/
	cd $(SERVER_PATH) && java -Xms1G -Xmx1G -jar -DIReallyKnowWhatIAmDoingISwear $(CRAFTBUKKIT_JAR_FILENAME)
