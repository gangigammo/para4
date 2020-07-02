JAVABIN=
#JAVABIN=/usr/lib/jvm/oracle-java10-jdk-amd64/bin/
#JAVANIB=/usr/lib/jvm/java-10-openjdk-amd64/bin/
#JAVABIN=/usr/lib/jvm/oracle-java9-jdk-amd64/bin/
JAVA=$(JAVABIN)java $(JAVAFLAGS)
JAVAC=$(JAVABIN)javac $(JAVACFLAGS)
JAVADOC=$(JAVABIN)javadoc $(JAVADOCFLAGS)
FIND=find
MKDIR=mkdir -p 
MAKE=make
RM=rm -rf

CLASSPATH=bin:lib/*
JAVACFLAGS= -encoding utf8 -d bin -sourcepath src -cp $(CLASSPATH) -Xlint:deprecation -Xdiags:verbose -Xlint:unchecked
JAVAFLAGS = -cp $(CLASSPATH):resource --illegal-access=deny
#JAVAFLAGS = -cp $(CLASSPATH):resource
#JAVADOCPROXY= -J-Dhttp.proxyHost=proxy.csc.titech.ac.jp -J-Dhttp.proxyPort=8080
JAVADOCPROXY=
JAVADOCFLAGS= -html5 -encoding utf-8 -charset utf-8 -package -d javadoc -sourcepath src $(JAVADOCPROXY) -link https://docs.oracle.com/javase/jp/10/docs/api -classpath $(CLASSPATH)

SERVADDR=localhost
NUMBER=`echo \`whoami\`0 | md5 | sed -e "s/[^0-9]//g" |cut -c 1-6`
PARAMETER=$(SERVADDR)

.PHONY: clean javadoc

.SUFFIXES: .class .java

.java.class:
	$(MKDIR) bin

.class:
	$(eval CLS := $(subst /,.,$(@:src/%=%)))
#	$(eval R := $(shell $(JAVAC) $@.java 2>&1))
	RES=`$(JAVAC) $@.java 2>&1` ; \
	if [ -z "$$RES" ]; then \
		$(JAVA) $(CLS)  $(PARAMETER); \
	else \
		echo "$$RES"; \
	fi;

ALL::	Calculator

Main07:
	$(MAKE) src/para/$@

Main08:
	$(MAKE) src/para/$@

Main09:
	$(MAKE) src/para/$@

Calculator:
	$(MAKE) src/para/calc/$@

clean:
	$(RM) bin javadoc

javadoc::
	$(FIND) . -name "._*" -exec $(RM) {} \;
	$(MKDIR) javadoc
	$(JAVADOC) para para.calc para.graphic.shape para.graphic.target para.graphic.parser para.graphic.camera
