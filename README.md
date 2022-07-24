# LASoT-Experiment : Learning to assert in software testing using mutants

## What is LASoT?

LASoT, which stands for *Learning to assert in software testing using mutants*, is the code name for my master thesis. I conducted an experiment base on two reporting tools for teaching software testing. One, [Descartes](https://github.com/STAMP-project/pitest-descartes), is a mutation engine plugin for PIT which implements extreme mutation and the other, [Reneri](https://github.com/STAMP-project/descartes-reneri), is a Maven plugin that generates hints to improve a test suite, by observing the execution of undetected extreme transformations discovered by Descartes in a Java project.  The goal of this experiment is to evaluate at which extent the inclusion of Descartes and Reneri reports in an IDE plugin can help students to refine the assertions of their tests. 

## What will you find in this repository?

In this GitHub repository, you will find in the [tarshes-descartes-reneri directory](./tarshes-descartes-reneri/) is a version of Reneri adapted by [Pierre Luycx](https://snail.info.unamur.be/author/pierre-luycx/).  You will also find [2048 directory](./2048) is an adapted version of the famous game 2048 and is the project on which we'll apply the [visual studio code lasot plugin](./tools/lasot-0.0.1.vsix).  There are also several batch files that will help you to install everything.

## How to install it?
- Install Visual Studio Code.
- Install JDK [11](https://adoptium.net/temurin/archive?version=11) or [17](https://adoptium.net/) version.
- Download this repository and extract.
- Execute install.bat file with admin rights.
- Reboot your computer.
- Execute start.bat file. Visual Studio Code will open with the 2048 project.

## What can I do now?

With the help off the LASoT plugin and the [usage indications](https://github.com/SamuelVandePut/LASoT) try to improve the tests suite based on the informations incorporated into the code about the survived mutations.  

> Tips : When you've change the tests suite, run it before starting the procedure.

When you have finished execute createReports.bat file.  This will create the archive you'll need to upload in the experiment [Google Form](https://docs.google.com/forms/d/e/1FAIpQLSfq9ZMw2bA00CK0hqz_ESFfbqTh4icy1YlXbLNdMPem2ewW5A/viewform?usp=sf_link).  Thank you to take time to fill in properly.  At last execute the uninstall batch script.  

Thank you!

## Who is behind this project?

[Samuel Van de Put](https://github.com/SamuelVandePut) master's student, from the University of Namur in Belgium. I was under the supervision of [Xavier Devroey](https://snail.info.unamur.be/author/xavier-devroey/) and [Benoît Vanderose](https://snail.info.unamur.be/author/benoit-vanderose/).

## Licensing

The dataset is licensed under LGPL3. This repository contains adapted source codes of [Reneri](https://github.com/STAMP-project/descartes-reneri) (LGPL3, Oscar Luis Vera Pérez) and an implementation of 2048 found on [Rosetta Code](https://www.rosettacode.org/wiki/2048#Java) (GFDL1.2, anonymous author).
