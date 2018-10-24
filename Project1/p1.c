/*
Write a program in C which functions as a DOS command interpreter.
DOS uses the commands cd, dir, type, del, ren, and copy to do the 
same functions as the UNIX commands cd, ls, cat, rm, mv, and cp. 
Your program loops continuously, allowing the user to type in DOS 
commands, which are stored in the variables command, arg1 and arg2. 
The command should be considered by a case statement, which executes
an appropriate UNIX command, depending on which DOS command has been 
given. The program should echo the following instruction to the user: 
Type Ctrl-C to exit. Include your program, along with output showing 
it run correctly.

@author: Joseph Allen
@date: 11/15/18

 TODO:
- Dynamically allocate space for input
- change strcpy and strcat to more secure strncpy and strncat
*/
#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>


// Function Declarations
char** parse_input(char input[]);
char** translate(char** args);
void execute(char** args);

int main (void){

    char input[300];
    pid_t pid;
    char** args;

    // Program loop
    while (true){
        printf("(Type CTRL+C to exit)$ ");
        if (fgets(input, 300, stdin) != NULL && input[0] != '\n'){
            args = parse_input(input);
            args = translate(args);
            pid = fork();
            if (pid == 0){
                execute(args);
            } else {
                wait(NULL);
            }
        }
    }

    return 0;
}
/*---------------------------------------------------------------------------*/
char** parse_input(char input[]){
    
    //Variables
    char* arg;
    char* rest;
    int num_args = 1;
    int current_arg = 0;

    // Count input args (including command)
    for (int i = 0; i < strlen(input); i++)
        if (input[i] == ' ') num_args++;

    // Allocate memory for a pointer to each token (and NULL at end)
    char **args = malloc(sizeof *args * (num_args + 1));

    // Get tokens
    arg = strtok_r(input, "\n", &rest);
    if (num_args == 1){                 // Command with no args
        args[current_arg] = malloc (sizeof *args * strlen(arg));
        strcpy(args[current_arg++], arg);
    } else {                            // Command with args
        arg = strtok_r(arg, " ", &rest);
        do {
            args[current_arg] = malloc(sizeof *args * strlen(arg));
            strcpy(args[current_arg++], arg);
        } while(arg = strtok_r(rest, " ", &rest));
    }
    args[current_arg] = NULL;

    // Verify tokens
    //for (int i = 0; i < num_args; i++) printf("ARG#%d: %s\n", i, args[i]);

    return args;
}
/*---------------------------------------------------------------------------*/
char** translate(char** args){

    char* trans;

    // Translate if an accepted DOS command
    if (strcmp(args[0], "dir") == 0) trans = "ls";
    else if (strcmp(args[0], "type") == 0) trans = "cat";
    else if (strcmp(args[0], "del") == 0) trans = "rm";
    else if (strcmp(args[0], "ren") == 0) trans = "mv";
    else if (strcmp(args[0], "copy") == 0) trans = "cp";
    else return args; // Command is cd or something we won't translate

    strcpy(args[0], trans); // Update the command
    
    return args;
}
/*---------------------------------------------------------------------------*/
void execute(char** args){

    // Use chdir to handle shell builtin cd
    if (strcmp(args[0], "cd") == 0) chdir(args[1]);

    // Otherwise, execute command in child process
    else execvp(args[0], args);
}
/*---------------------------------------------------------------------------*/