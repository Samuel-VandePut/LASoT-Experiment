# LASoT-Experiment : Learning to assert in software testing using mutants

## What is LASoT?

LASoT, which stands for *Learning to assert in software testing using mutants*, is the code name for my master thesis. I conducted an experiment base on two reporting tools for teaching software testing to undergraduates. One, [Descartes](https://github.com/STAMP-project/pitest-descartes), is a mutation engine plugin for PIT which implements extreme mutation and the other, [Reneri](https://github.com/STAMP-project/descartes-reneri), is a Maven plugin that generates hints to improve a test suite, by observing the execution of undetected extreme transformations discovered by Descartes in a Java project.  The goal of this experiment is to evaluate at wich extent the inclusion of Descartes and Reneri reports in an IDE plugin can help students to refine the assertions of their tests. 

## What will you find in this repository?

In this GitHub repository, you will find in the [tarshes-descartes-reneri directory](./tarshes-descartes-reneri/) is a version of Reneri adapted by [Pierre Luycx](https://snail.info.unamur.be/author/pierre-luycx/).  You will also find [2048 directory](./2048) is an adapted version of the famous game 2048 and is the project on which we'll apply the [visual studio code lasot plugin](./tools/lasot-0.0.1.vsix).  There is also a [tools directory](./tools) which contains a java version and several batch files that will help you to install everything.

## How to install it?
- Install Visual Studio Code.
- Clone this repository.
- Execute install.bat file with admin rights.
- Reboot your computer.
- Excute start.bat file.

## Who is behind this project?

I [Samuel Van de Put](https://github.com/SamuelVandePut) master's student, from the University of Namur in Belgium. I was under the supervision of [Xavier Devroey](https://snail.info.unamur.be/author/xavier-devroey/) and [Benoît Vanderose](https://snail.info.unamur.be/author/benoit-vanderose/).

## Licensing

The dataset is licensed under LGPL3. This repository contains adapted source codes of [Reneri](https://github.com/STAMP-project/descartes-reneri) (LGPL3, Oscar Luis Vera Pérez) and an implementation of 2048 found on [Rosetta Code](https://www.rosettacode.org/wiki/2048#Java) (GFDL1.2, anonymous author).