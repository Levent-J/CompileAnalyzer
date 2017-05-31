package com.levent_j;

import jdk.internal.org.objectweb.asm.tree.analysis.Analyzer;

import java.io.*;

import static com.levent_j.Constants.*;

/**
 * Created by levent_j on 17-5-7.
 */
public class LexcialAnalyzer {

    private static LexcialAnalyzer sInstance;

    private FileInputStream fileInputStream;
    private InputStreamReader inputStreamReader;
    private String result = "";
    private String errorMsg = "";
    private String content = "";
    private int currentIndex = 0;
    private boolean eof = false;

    public static LexcialAnalyzer getsInstance(){
        if (sInstance==null){
            sInstance = new LexcialAnalyzer();
        }
        return sInstance;
    }

    private LexcialAnalyzer(){
        //init
        File file = new File(PATH, LEX_IN_FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public LexcialAnalyzer loadText(){
        //先将字符全部读入到content中
        try {
            int ch = 0;
            while ((ch = inputStreamReader.read()) != -1){
                content += (char)ch;
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    public LexcialAnalyzer analyzer() {
        while (doLexAnalyze()){}
        System.out.println("词法分析完毕");
        return this;
    }


    /*词法分析函数，每调用一次识别一个符号*/
    private boolean doLexAnalyze() {
        int lineNum = 1;
        char character;
        String token = "";
        character = getFirstNotNullChar();

        switch (character) {
            case '\n':
                output(CHAR_EOLN, 24);
                lineNum++;
                break;
            case ' ':
                output(CHAR_EOF, 25);
                return false;//false表示已读到文件末尾
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':

                while ((Character.isLetter(character)) || Character.isDigit(character)) {
                    token += character;
                    character = getNextChar();
                }
                retract(character);
                int kind;
                kind = getTokenKind(token);
                if (kind != 0){
                    output(token, kind);
                } else {
                    int val;
                    val = symbol();
                    output(token, val);
                }
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                while (Character.isDigit(character)) {
                    token += character;
                    character = getNextChar();
                }
                retract(character);
                output(token, constant());
                break;
            case '=':
                output("=", 12);
                break;
            case '<':
                character = getNextChar();
                if (character == '>') {
                    output("<>", 13);
                } else if (character == '='){
                    output("<=", 14);
                }else {
                    retract(character);
                    output("<", 15);
                }
                break;
            case '>':
                character = getNextChar();
                if (character == '=') {
                    output(">=", 16);
                } else {
                    retract(character);
                    output(">", 17);
                }
                break;
            case '-':
                output("-", 18);
                break;
            case '*':
                output("*", 19);
                break;
            case ':':
                character = getNextChar();
                if (character == '=')
                    output(":=", 20);
                else
                    error(lineNum, 2);//输出“未知运算符”错误
                break;
            case '(':
                output("(", 21);
                break;
            case ')':
                output(")", 22);
                break;
            case ';':
                output(";", 23);
                break;
            default:
                error(lineNum, 1);//输出"出现字母表以外的非法字符"错误
        }
        return true;
    }

    //常数
    private int constant() {
        return 11;
    }

    //标识符
    private int symbol() {
        return 10;
    }

    private int getTokenKind(String token) {
        if (token.equals(CHAR_BEGIN))
            return 1;
        else if (token.equals(CHAR_END))
            return 2;
        else if (token.equals(CHAR_INTEGER))
            return 3;
        else if (token.equals(CHAR_INTEGER))
            return 4;
        else if (token.equals(CHAR_THEN))
            return 5;
        else if (token.equals(CHAR_ELSE))
            return 6;
        else if (token.equals(CHAR_FUNCTION))
            return 7;
        else if (token.equals(CHAR_READ))
            return 8;
        else if (token.equals(CHAR_WRITE))
            return 9;
        else
            return 0;
    }

    private void error(int lineNum, int i) {
        errorMsg += "第" + lineNum + "行有错 " + i + "\n";

    }

    private void output(String s, int i) {
        result += (s + " " + i) + "\n";
    }

    public char getNextChar() {
        if (currentIndex>=content.length()){
            eof = true;
            return ' ';
        }
        char ch = content.charAt(currentIndex);
        currentIndex ++;
        return ch;
    }

    public char getFirstNotNullChar() {
        char ch = getNextChar();
        while (!eof) {
            if (ch == '\r' || ch == '\t' || ch == ' ') {
                ch = getNextChar();
            } else {
                break;
            }
        }
        return ch;
    }

    private void retract(char character) {
        //回退
        currentIndex--;
    }

    public LexcialAnalyzer outputText() {
        File file = new File(PATH, LEX_OUT_FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            byte[] bytes = new byte[1024];
            bytes = result.getBytes();
            FileOutputStream outputStream = new FileOutputStream(file);
            try {
                outputStream.write(bytes,0,bytes.length);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return this;
    }

    public void outputError() {
        File file = new File(PATH, LEX_ERROR_OUT_FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            byte[] bytes = new byte[1024];
            bytes = errorMsg.getBytes();
            FileOutputStream outputStream = new FileOutputStream(file);
            try {
                outputStream.write(bytes,0,bytes.length);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
