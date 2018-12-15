[1mdiff --git a/demos/demos.iml b/demos/demos.iml[m
[1mindex 4a61bfc..4f323ac 100644[m
[1m--- a/demos/demos.iml[m
[1m+++ b/demos/demos.iml[m
[36m@@ -15,6 +15,9 @@[m
         </sourceRoots>[m
       </configuration>[m
     </facet>[m
[32m+[m[32m    <facet type="Spring" name="Spring">[m
[32m+[m[32m      <configuration />[m
[32m+[m[32m    </facet>[m
   </component>[m
   <component name="NewModuleRootManager" LANGUAGE_LEVEL="JDK_1_8">[m
     <output url="file://$MODULE_DIR$/target/classes" />[m
[36m@@ -65,12 +68,9 @@[m
     <orderEntry type="library" name="Maven: commons-cli:commons-cli:1.2" level="project" />[m
     <orderEntry type="library" name="Maven: org.apache.commons:commons-math:2.1" level="project" />[m
     <orderEntry type="library" name="Maven: xmlenc:xmlenc:0.52" level="project" />[m
[31m-    <orderEntry type="library" name="Maven: commons-io:commons-io:2.4" level="project" />[m
     <orderEntry type="library" name="Maven: commons-lang:commons-lang:2.5" level="project" />[m
     <orderEntry type="library" name="Maven: commons-configuration:commons-configuration:1.6" level="project" />[m
[31m-    <orderEntry type="library" name="Maven: commons-collections:commons-collections:3.2.1" level="project" />[m
     <orderEntry type="library" name="Maven: commons-digester:commons-digester:1.8" level="project" />[m
[31m-    <orderEntry type="library" name="Maven: commons-beanutils:commons-beanutils:1.7.0" level="project" />[m
     <orderEntry type="library" name="Maven: commons-beanutils:commons-beanutils-core:1.8.0" level="project" />[m
     <orderEntry type="library" name="Maven: com.google.protobuf:protobuf-java:2.5.0" level="project" />[m
     <orderEntry type="library" name="Maven: org.apache.hadoop:hadoop-auth:2.2.0" level="project" />[m
[36m@@ -83,7 +83,6 @@[m
     <orderEntry type="library" name="Maven: org.apache.hadoop:hadoop-yarn-client:2.2.0" level="project" />[m
     <orderEntry type="library" name="Maven: com.google.inject:guice:3.0" level="project" />[m
     <orderEntry type="library" name="Maven: javax.inject:javax.inject:1" level="project" />[m
[31m-    <orderEntry type="library" name="Maven: aopalliance:aopalliance:1.0" level="project" />[m
     <orderEntry type="library" name="Maven: org.apache.hadoop:hadoop-yarn-server-common:2.2.0" level="project" />[m
     <orderEntry type="library" name="Maven: org.apache.hadoop:hadoop-mapreduce-client-shuffle:2.2.0" level="project" />[m
     <orderEntry type="library" name="Maven: org.apache.hadoop:hadoop-yarn-api:2.2.0" level="project" />[m
[36m@@ -149,5 +148,109 @@[m
     <orderEntry type="library" name="Maven: org.scalatest:scalatest_2.11:2.2.6" level="project" />[m
     <orderEntry type="library" name="Maven: org.scala-lang.modules:scala-xml_2.11:1.0.2" level="project" />[m
     <orderEntry type="library" name="Maven: org.spark-project.spark:unused:1.0.0" level="project" />[m
[32m+[m[32m    <orderEntry type="library" name="Maven: com.jeesite:jeesite-framework:4.0-SNAPSHOT" level="project" />[m
[32m+[m[32m    <orderEntry type="library" name="Maven: com.jeesite:jeesite-common:4.0-SNAPSHOT" level="project" />[m
[32m+[m[32m    <orderEntry type="library" name="Maven: commons-io:commons-io:2.4" level="project" />[m
[32m+[m[32m    <orderEntry type="library" name="Maven: commons-fileupload:commons-fileupload:1.3.1" level="project" />[m
[32m+[m[32m    <orderEntry type="library" name="Maven: commons-beanutils:commons-beanutils:1.9.3" level="project" />[m
[32m+[m[32m    <orderEntry type="library" name="Maven: commons-collections:commons-collections:3.2.2" level="project" />[m
[32m+[m[32m    <orderEntry type="library" name="Maven: de.ruedigermoeller:fst:2.56" level="project" />[m
[32m+[m[32m    <orderEntry type="library" name="Maven: org.json:json:20170516" level="project" />[m
[32m+[m[32m    <orderEntry type="library" name="Maven: com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.8.10" level="project" />[m
[32m+[m[32m    <orderEntry type="library" name="Maven: com.fa