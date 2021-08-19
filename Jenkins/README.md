# Premember deploy notes
Repository containing Jenkins and related files for UHC - Premember build

# Jenkins information   
Jenkins
   https://jenkins-premember.origin-elr-core-nonprod.optum.com
Openshift
   https://origin-elr-core-nonprod.optum.com/console/project/premember/overview
User groups 
   pre_member_jenkins_admin

# Interesting bits...
Mirum has a tidy and elegant build process that does many things (see the READ.ME in the project root). Unfortunately the wrapped Maven build does not play well in our infrastrucure, spedcifically the NPM install. The wrapped Maven build wants to use the default NPM registry https://registry.npmjs.org/npm/ that is blocked internally. In this folder is a copy of the root pom.xml file and has reconfigured the repo location.  This Jenkins pom file MUST be copied into the working Jenkins directory BEFORE the mvnw command is executed. Any changes to the root pom file shoud be manually performed to the pom file in this folder. 