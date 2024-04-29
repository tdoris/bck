package BCK.ANN;

import java.io.*;

public class BCKStreamTokenizer extends StreamTokenizer {

    public BCKStreamTokenizer(Reader r){
        super(r);
    }

    public void eatEOL() throws IOException{
        if(nextToken()!=TT_EOL){
            throw new IOException("EOL expected, not found!");
        }
    }

    public double getNumber() throws IOException{
        if(nextToken()==TT_NUMBER){
            return nval;
        } 
        else {
            throw new IOException("Number Expected, not found!");
        }
    }

    public String getWord() throws IOException{
        if(nextToken()==TT_WORD){
            return sval.toString();
        } 
        else {
            throw new IOException("Word Expected, not found!");
        }
    }

    public void eatNumber(double n) throws IOException{
        if(n!=getNumber()){
            throw new IOException("Tried to eat '"+n+"', got "+nval);
        }
    }
    public void eatString(String food) throws IOException{ //read and discard food
        if(!food.equals(getWord())){
            throw new IOException("Tried to eat '"+food+"', got "+sval.toString()+" instead.");
        }
    }

    public void eatChar(char ch) throws IOException {
        int type=nextToken();
        if(type==TT_EOL){
            throw new IOException("Tried to eat character: '"+ch+"', got : an EOL");
        }
        if(type==TT_EOF){
            throw new IOException("Tried to eat character: '"+ch+"', got : an EOF");
        }
        if(type==TT_NUMBER){
            throw new IOException("Tried to eat character: '"+ch+"', got : a NUMBER :"+nval);
        }
        if(type==TT_WORD){
            throw new IOException("Tried to eat character: '"+ch+"', got : a WORD");
        }
        if(ttype!=ch){
            throw new IOException("Tried to eat character: '"+ch+"', got : "+ttype);
        }
    }
    public void skip() throws IOException{
        nextToken();
    }

    public void newLine() throws IOException{ //skip all tokens to next line
        while(nextToken()!=TT_EOL){
            //better check for premature EOF
            if(ttype==TT_EOF){
                throw new IOException("EOF before EOL in newLine()");
            }
        }
    }
}








