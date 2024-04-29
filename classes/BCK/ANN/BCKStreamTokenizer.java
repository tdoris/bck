package BCK.ANN;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

public class BCKStreamTokenizer extends StreamTokenizer {
   public BCKStreamTokenizer(Reader var1) {
      super(var1);
   }

   public void eatEOL() throws IOException {
      if (this.nextToken() != 10) {
         throw new IOException("EOL expected, not found!");
      }
   }

   public double getNumber() throws IOException {
      if (this.nextToken() == -2) {
         return super.nval;
      } else {
         throw new IOException("Number Expected, not found!");
      }
   }

   public String getWord() throws IOException {
      if (this.nextToken() == -3) {
         return super.sval.toString();
      } else {
         throw new IOException("Word Expected, not found!");
      }
   }

   public void eatNumber(double var1) throws IOException {
      if (var1 != this.getNumber()) {
         throw new IOException("Tried to eat '" + var1 + "', got " + super.nval);
      }
   }

   public void eatString(String var1) throws IOException {
      if (!var1.equals(this.getWord())) {
         throw new IOException("Tried to eat '" + var1 + "', got " + super.sval.toString() + " instead.");
      }
   }

   public void eatChar(char var1) throws IOException {
      int var2 = this.nextToken();
      if (var2 == 10) {
         throw new IOException("Tried to eat character: '" + var1 + "', got : an EOL");
      } else if (var2 == -1) {
         throw new IOException("Tried to eat character: '" + var1 + "', got : an EOF");
      } else if (var2 == -2) {
         throw new IOException("Tried to eat character: '" + var1 + "', got : a NUMBER :" + super.nval);
      } else if (var2 == -3) {
         throw new IOException("Tried to eat character: '" + var1 + "', got : a WORD");
      } else if (super.ttype != var1) {
         throw new IOException("Tried to eat character: '" + var1 + "', got : " + super.ttype);
      }
   }

   public void skip() throws IOException {
      this.nextToken();
   }

   public void newLine() throws IOException {
      while (this.nextToken() != 10) {
         if (super.ttype == -1) {
            throw new IOException("EOF before EOL in newLine()");
         }
      }
   }
}
