#! /bin/bash
#author Ryan Torok
#date 5/30/18
#purpose: launch paintbrush gui
cd ~/paintbrush_lms;

/home/rtorok/lib/java-10-openjdk-amd64/bin/java -javaagent:/home/rtorok/Downloads/idea-IU-183.5429.30/lib/idea_rt.jar=39635:/home/rtorok/Downloads/idea-IU-183.5429.30/bin -Dfile.encoding=UTF-8 -classpath /home/rtorok/paintbrush_lms/out/production/paintbrush_lms:/home/rtorok/Downloads/idea-IU-183.5429.30/plugins/testng/lib/testng.jar:/home/rtorok/Downloads/idea-IU-183.5429.30/plugins/testng/lib/jcommander.jar:/home/rtorok/Downloads/idea-IU-183.5429.30/lib/junit-4.12.jar:/home/rtorok/Downloads/idea-IU-183.5429.30/lib/hamcrest-core-1.3.jar:/home/rtorok/paintbrush_lms/lib/org.osgi.core-6.0.0.jar:/home/rtorok/paintbrush_lms/lib/jgrapht-jdk1.5-0.7.3.jar:/home/rtorok/paintbrush_lms/lib/richtextfx-0.9.2.jar:/home/rtorok/paintbrush_lms/lib/reactfx-2.0-M5.jar:/home/rtorok/paintbrush_lms/lib/undofx-2.1.0.jar:/home/rtorok/paintbrush_lms/lib/flowless-0.6.jar:/home/rtorok/paintbrush_lms/lib/wellbehavedfx-0.3.3.jar:/home/rtorok/.IntelliJIdea2018.3/config/jdbc-drivers/MySQL\ Connector/J/5.1.46/mysql-connector-java-5.1.46.jar main.RunAll

