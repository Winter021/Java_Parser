import java.io.*;
import java.util.*;

public class parser {
  public static int i;
  public static int lno;
  public static int lsz;
  public static String[] lines;
  public static Map<String, List<Integer>> globalmap = new HashMap<String, List<Integer>>();
  public static Stack<Character> charStack = new Stack<>();

  public static void skipwhitespace(){
    if(lno >= lsz)return;
    String l = lines[lno];
    int n = l.length();
    while(i<n && l.charAt(i) == ' '){
      i++;
    }
    if(i == n){
      i = 0;
      lno++;
      if(lno >= lsz)return;
      skipwhitespace();
    }
  }

  
  public static boolean checkalphanum(char ch){
    if(( ch>='A' && ch<='Z')||( ch>='a' && ch<='z') || (ch>='0' && ch<='9') || (ch == '_')){
      return true;
    }
    return false;
  }

  public static String gettoken(){
    skipwhitespace();
    if(lno >= lsz){
      return "";
    }
    String curr = "";
    String l = lines[lno];
    
    
    int n = l.length();

    while(i<n && checkalphanum(l.charAt(i))){
      curr += l.charAt(i);
      i++;
    }

    if(i < n && !curr.equals("") && (l.charAt(i) == '.')){
      curr += '.';
      i++;
      while(i<n && checkalphanum(l.charAt(i))){
        curr += l.charAt(i);
        i++;
      }
      // System.out.println(lno+1 + " "+i);
      // System.out.println(curr);
      return curr;
    }

    if(i == n && curr.equals("")){
      i = 0;
      lno++;
      return gettoken();
    }

    if(curr.equals("")){
      curr += l.charAt(i);
      i++;
      if(i == n){
        // System.out.println(lno+1 + " "+i);
        // System.out.println(curr);
        return curr;
      }
    }
    if(curr.equals("\"")){
      while(l.charAt(i) != '\"'){
        curr += l.charAt(i);
        i++;
      }
      curr += l.charAt(i);
      i++;
      // System.out.println(lno+1 + " "+i);
      // System.out.println(curr);
      return curr;
    }

    if(curr.charAt(0) == '\''){
      
      while(i<n && l.charAt(i) != '\'' ){
        curr += l.charAt(i);
        i++; 
      }
      curr += l.charAt(i);
      i++;
      
      if(curr.length() != 3){
        System.out.println(lno+1+": Not a valid token i.e. not a character");
      }
      // System.out.println(lno+1 + " "+i);
      // System.out.println(curr);
      return curr;
    }

    if(curr.equals("=") && (l.charAt(i) == '=' )){
      curr += l.charAt(i);
      i++;
    }
    if(curr.equals("+") && (l.charAt(i) == '=' )){
      curr += l.charAt(i);
      i++;
    }
    if(curr.equals("-") && (l.charAt(i) == '=' )){
      curr += l.charAt(i);
      i++;
    }
    if(curr.equals("*") && (l.charAt(i) == '=' )){
      curr += l.charAt(i);
      i++;
    }
    if(curr.equals("!") && (l.charAt(i) == '=' )){
      curr += l.charAt(i);
      i++;
    }
    if(curr.equals("/") && (l.charAt(i) == '=' )){
      curr += l.charAt(i);
      i++;
    }
    if(curr.equals("+") && (l.charAt(i) == '+' )){
      curr += l.charAt(i);
      i++;
    }
    if(curr.equals("-") && (l.charAt(i) == '-' )){
      curr += l.charAt(i);
      i++;
    }
    if(curr.equals(">") && (l.charAt(i) == '=' )){
      curr += l.charAt(i);
      i++;
    }
    if(curr.equals("<") && (l.charAt(i) == '=' )){
      curr += l.charAt(i);
      i++;
    }
    if(curr.equals("&") && (l.charAt(i) == '=' )){
      curr += l.charAt(i);
      i++;
    }
    if(curr.equals("|") && (l.charAt(i) == '=' )){
      curr += l.charAt(i);
      i++;
    }

    // System.out.println(lno+1 + " "+i);
    // System.out.println(curr);
    return curr;
  }

  public static boolean match(String l, String str){
    int sz = str.length();
    int n = l.length();
    for(int j = 0; j<sz; j++){
      if(i >= n)return false;
      if(i<n && l.charAt(i) != str.charAt(j)){
        return false;
      }
      i++;
    }
    return true;
  }

  public static void checkheader(String l){
      
      i++;
      int n = l.length();
      skipwhitespace();


      if(match(l, "include")){
        skipwhitespace();
        if(l.charAt(i) == '<'){
          while(i<n && l.charAt(i) != '>'){
            i++;
          }
          if(i == n){
            System.out.println(lno+1+": Header error!!! '>' missing");
          }
         
          i++;
          if(i < n){
            System.out.println(lno+1+": Header error!!!");
            return;
          }
        }else{
          System.out.println(lno+1+": Header error!!! '<' missing");
        }
      }else{
        System.out.println(lno+1+": Header error!!!");
      }
    
  }

  public static void checkop(String curr){
    if(curr.equals("{") ){
      charStack.push('{');
    }else if(curr.equals("(") ){
      charStack.push('(' );
    }
  }

  public static void checkcp(String curr){
    
    if(curr.equals("}")  && (!charStack.empty() && charStack.peek() == '{') ){
      charStack.pop();
    }else if(curr.equals(")")  && (!charStack.empty() && charStack.peek() == '(') ){
      charStack.pop();
    }else{
      System.out.println(lno+1+": Unmatched Parenthesis");
      charStack.pop();
    }
  }

  public static boolean checkkw(String curr){

    if(curr.equals("char") ||curr.equals("else") || curr.equals("if") ||curr.equals("do") ||curr.equals("while") ||curr.equals("for") || curr.equals("long") ||curr.equals("string") || curr.equals("double") || curr.equals("float") || curr.equals("int")  ){
      return true;
    }

    return false;
  }

  public static boolean checkid(int scope){
    String id = gettoken();
    if(id.equals("long") || id.equals("int")){
      id = gettoken();
    }
    if(id.equals("int")){
      id = gettoken();
    }

    if(checkkw(id)){
      System.out.println(lno+1+": "+id+" is not a valid identifier");
      return true;
    }

    if(id.equals(";")){
      return false;
    }
    if((id.charAt(0)<'a' || id.charAt(0)>'z') && ( id.charAt(0)<'A' || id.charAt(0)>'Z' ) ){
      System.out.println(lno+1+": "+id+" is not a valid identifier");
    }else{
      for (Map.Entry<String, List<Integer>> me : globalmap.entrySet()) {
        if(id.equals(me.getKey())){
          List<Integer> arr = me.getValue();
          int size = arr.size();
          for(int lll = 0; lll<size; lll++){
            if(scope == arr.get(lll)){
              System.out.println(lno+1+": "+id+" declared again..!!");
              return true;
            }
          }
        }
      }
      if(globalmap.get(id) != null){
        globalmap.get(id).add(scope);
      }else{
        List<Integer> x = new ArrayList<Integer>();
        x.add(scope);
        globalmap.put(id,  x);
      }
      
    }
    return true;
  }
  public static boolean isid(String curr){
    int n = curr.length();
    int k = 0;
    while(k < n){
      char ch = curr.charAt(k);
      if(!( ch>='A' && ch<='Z') && !( ch>='a' && ch<='z') && !(ch>='0' && ch<='9') && !(ch == '_')){
        return false;
      }
      k++;
    }
    return true;
  }

  public static boolean isconst(String curr){
    int n = curr.length();
    int k = 0;
    int once = 0;
    while(k < n){
      char ch = curr.charAt(k);
      if( (!(ch>='0' && ch<='9')) && (!(once == 0 && ch == '.')) ){
        return false;
      }
      if(ch == '.')once++;
      k++;
    }
    return true;
  }

  public static void checkotherdecl(String curr, int scope){

    if(!checkid(scope)){
      System.out.println(lno+1+": Not a valid statement..!");
      return;
    }
    curr = gettoken();
    if(curr.equals(";")){
      return;
    }else if(curr.equals("=")){

      curr = gettoken();
      if(curr.equals(";")){
        System.out.println(lno+1+": Not a valid declaration statement..!");
        System.exit(0);
      }else if(curr.charAt(0) == '\"'){
        System.out.println(lno+1+": Cannot equate string to this variable..!");
        curr = gettoken();
        while(!curr.equals(";")){
          curr = gettoken();
          if(curr.equals(""))break;
        }
      }else if(curr.charAt(0) == '\''){
        System.out.println(lno+1+": Cannot equate char to this variable..!");
        curr = gettoken();
        while(!curr.equals(";")){
          curr = gettoken();
          if(curr.equals(""))break;
        }
      }else if(isconst(curr)){
        curr = gettoken();
        if(curr.equals(";")){
          return;
        }else{
          System.out.println(lno+1+": is not a valid declaration statement..!");
          System.exit(0);
        }
        return;
      }else if(isid(curr)){
        if(globalmap.get(curr) == null){
          System.out.println(lno+1+": "+" "+curr+" is not a declared variable to equate..!");
        }
        curr = gettoken();
        if(!curr.equals(";")){
          System.out.println(lno+1+": is not a valid declaration statement..!");
          while(!curr.equals(";")){
            curr = gettoken();
            if(curr.equals(""))break;
          }
        }
      }else{
        System.out.println(lno+1+": "+" "+curr+" is not a valid variable or constant to equate..!");
        curr = gettoken();
        if(!curr.equals(";")){
          System.out.println(lno+1+": is not a valid declaration statement..!");
          while(!curr.equals(";")){
            curr = gettoken();
            if(curr.equals(""))break;
          }
        }
      }

    }else{
      System.out.println(lno+1+": is not a valid declaration statement..!");
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals(""))break;
            }
    }

  }

  public static boolean checkdeclarations(String curr, int scope){
    
    if(curr.equals("string") ){
      
      if(!checkid(scope)){
        System.out.println(lno+1+": is not a valid declaration statement..!");
      }
      curr = gettoken();
      if(curr.equals(";")){
        return true;
      }else if(curr.equals("=")){

        curr = gettoken();
        if(curr.equals(";")){
          System.out.println(lno+1+": is not a valid declaration statement..!");
        }else if(curr.charAt(0) == '\"'){

          curr = gettoken();
          if(!curr.equals(";")){
            System.out.println(lno+1+": is not a valid declaration statement..!");
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals("")){
                return false;
              }
            }
          }
        }else if(curr.charAt(0) == '\''){
          System.out.println(lno+1+": Cannot assign char to a string variable..! ");
          curr = gettoken();
          if(!curr.equals(";")){
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals("")){
                return false;
              }
            }
          }
        }else if(isconst(curr)){
          System.out.println(lno+1+": String cannot be equated to constant number..! ");
          while(!curr.equals(";")){
            curr = gettoken();
            if(curr.equals("")){
              return false;
            }
          }
        }else if(isid(curr)){
          if(globalmap.get(curr) == null){
            System.out.println(lno+1+": "+" "+curr+" is not a declared variable to equate..!");
          }
          curr = gettoken();
          if(!curr.equals(";")){
            System.out.println(lno+1+": is not a valid declaration statement..!");
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals("")){
                return false;
              }
            }
          }
        }else{
          System.out.println(lno+1+": "+" "+curr+" is not a valid variable or constant to equate..!");
          curr = gettoken();
          if(!curr.equals(";")){
            System.out.println(lno+1+": is not a valid declaration statement..!");
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals("")){
                return false;
              }
            }
          }
        }

      }else{
        System.out.println(lno+1+": is not a valid declaration statement..!");
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals(""))break;
            }
      }
      return true;
    }else if(curr.equals("int") ){
      checkotherdecl(curr, scope);
      return true;
    }else if(curr.equals("long") ){
      checkotherdecl(curr, scope);
      return true;
    }else if(curr.equals("double") ){
      checkotherdecl(curr, scope);
      return true;
    }else if(curr.equals("float")){
      checkotherdecl(curr, scope);
      return true;
    }else if(curr.equals("char")){
      if(!checkid(scope)){
        System.out.println(lno+1+": is not a valid declaration statement..!");
      }
      curr = gettoken();
      if(curr.equals(";")){
        return true;
      }else if(curr.equals("=")){
  
        curr = gettoken();
        if(curr.equals(";")){
          System.out.println(lno+1+": is not a valid declaration statement..!");
        }else if(curr.charAt(0) == '\"'){
          System.out.println(lno+1+": Cannot equate string to this variable..!");
          curr = gettoken();
          while(!curr.equals(";")){
            curr = gettoken();
            if(curr.equals(""))break;
          }
        }else if(curr.charAt(0) == '\''){
          
          curr = gettoken();
          while(!curr.equals(";")){
            curr = gettoken();
            if(curr.equals(""))break;
          }
        }else if(isconst(curr)){
          curr = gettoken();
          if(curr.equals(";")){
            return true;
          }else{
            System.out.println(lno+1+": Not a valid statement..! ");
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals(""))break;
            }
          }
        }else if(isid(curr)){
          if(globalmap.get(curr) == null){
            System.out.println(lno+1+": "+" "+curr+" is not a declared variable to equate..!");
          }
          curr = gettoken();
          if(!curr.equals(";")){
            System.out.println(lno+1+": Not a valid statement..! ");
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals(""))break;
            }
          }
        }else{
          System.out.println(lno+1+": "+" "+curr+" is not a valid variable or constant to equate..!");
          curr = gettoken();
          if(!curr.equals(";")){
            System.out.println(lno+1+": Not a valid statement..! ");
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals(""))break;
            }
          }
        }
  
      }else{
        System.out.println(lno+1+": Not a valid statement..! ");
        while(!curr.equals(";")){
          curr = gettoken();
          if(curr.equals(""))break;
        }
      }
      return true;
    }else{
      return false;
    }
    // else if(curr == "short"){
    //   return true;
    // }else if(curr == "signed"){
    //   return true;
    // }else if(curr == "unsigned"){
    //   return true;
    // }
  }

  public static boolean checkvalidid(String curr, int scope){
    
    if(checkkw(curr)){
      return false;
    }
    if((curr.charAt(0)<'a' || curr.charAt(0)>'z') && ( curr.charAt(0)<'A' || curr.charAt(0)>'Z' ) ){
      return false;
    }else{
      int flag1 = 0;
      for (Map.Entry<String, List<Integer>> me : globalmap.entrySet()) {
        if(curr.equals(me.getKey())){
          List<Integer> arr = me.getValue();
          int size = arr.size();
          for(int lll = 0; lll<size; lll++){
            if(scope >= arr.get(lll)){
              flag1 = 1;
              break;
            }
          }
        }
        if(flag1 == 1){
          break;
        }
      }

      if(flag1 != 1){
        System.out.println(lno+1+": "+curr+"- Undeclared Variable has been used..!");
        return false;
      }else{
        return true;
      }
    }
  }

  public static boolean checkvalidconstid2(String curr, int scope){
    if(checkkw(curr)){
      return false;
    }

    if(isconst(curr)){
      return true;
    }

    if((curr.charAt(0)<'a' || curr.charAt(0)>'z') && ( curr.charAt(0)<'A' || curr.charAt(0)>'Z' ) ){
      return false;
    }else{
      int flag1 = 0;
      for (Map.Entry<String, List<Integer>> me : globalmap.entrySet()) {
        if(curr.equals(me.getKey())){
          List<Integer> arr = me.getValue();
          int size = arr.size();
          for(int lll = 0; lll<size; lll++){
            if(scope >= arr.get(lll)){
              flag1 = 1;
              break;
            }
          }
        }
        if(flag1 == 1){
          break;
        }
      }

      if(flag1 != 1){
        System.out.println(lno+1+": "+" "+curr+"- Undeclared variable has been used..!");
      }
      return true;
    }
  }

  public static void checkfor1(int scope){
    if(checkboolexpr(scope, ";")){
    }else{
      System.out.println(lno+1+": Missing for boolean condition..!");
    }

    String curr = gettoken();
    if(curr.equals(";")){
      System.out.println(lno+1+": Wrong for format extra ; (statement concluder) present..!");
    }else if(curr.equals(")")){
      checkcp(curr);
      curr = gettoken();

      if(curr.equals("{")){
        checkop(curr);
        checkstatements(scope);
        clearscopevar(scope-1);
      }else{
        checkstatements2(curr, scope);
      }
    }else if(curr.equals("{") ){
      System.out.println(lno+1+": Missing ) parenthesis..!");
      System.out.println(lno+1+": Incorrect for format..!");
      System.exit(0);
    }else{
      checkassignstmt2(curr, scope);
      checkcp(")");
      curr = gettoken();

      if(curr.equals("{")){
        checkop(curr);
        checkstatements(scope);
        clearscopevar(scope-1);
      }else{
        checkstatements2(curr, scope);
      }
    }
  }

  public static void checkfor(int scope){
    String curr = gettoken();

    if(curr.equals("(")){
      checkop(curr);

      curr = gettoken();

      if(curr.equals("char") ||curr.equals("else") || curr.equals("if") ||curr.equals("do") ||curr.equals("while") ||curr.equals("for") || curr.equals("long") ||curr.equals("string") || curr.equals("double") || curr.equals("float") || curr.equals("int")){
        if( curr.equals("else") || curr.equals("if") ||curr.equals("do") ||curr.equals("while") ||curr.equals("for")  ){
          System.out.println(lno+1+": Wrong for format..!");
          System.exit(0);;
        }else{
          checkdeclarations(curr, scope);
          checkfor1(scope);
        }
      }else if(curr.equals(";")){
        checkfor1(scope);
      }else if(checkassignstmt(curr, scope)){
        checkfor1(scope);
      }else{
        System.out.println(lno+1+": Wrong for format..!");
        System.exit(0);
      }
    }else{
      System.out.println(lno+1+": Wrong for format..!");
      System.exit(0);
    }
  }

  public static void checkwhile(int scope){
    String curr = gettoken();

    if(curr.equals("(")){
      checkop(curr);

      if(checkboolexpr(scope, "{")){
      }else{
        System.out.println( lno+1+": while condition missing..!" );
      }     
        checkstatements(scope+1);
        clearscopevar(scope);
        checkstatements(scope);
    }else{
      System.out.println( lno+1+": Missing ( bracket for while format..!" );
      checkop("(");
      if(checkboolexpr(scope, "{")){
      }else{
        System.out.println( lno+1+": while condition missing..!" );
      }     
        checkstatements(scope+1);
        clearscopevar(scope);
        checkstatements(scope);
    }
  }

  public static boolean checkassignstmt(String curr, int scope){
    
    if(checkkw(curr)){
      System.out.println(lno+1+": "+curr+" is not a valid identifier");
      while(!curr.equals(";")){
        curr = gettoken();
        if(curr.equals("")){
          break;
        }
      }
      return false;
    }

    if((curr.charAt(0)<'a' || curr.charAt(0)>'z') && ( curr.charAt(0)<'A' || curr.charAt(0)>'Z' ) ){
      System.out.println(lno+1+": "+curr+" is not a valid identifier");
      
      while(!curr.equals(";")){
        curr = gettoken();
        if(curr.equals("")){
          break;
        }
      }
      return false;
    }else{
      int flag1 = 0;
      for (Map.Entry<String, List<Integer>> me : globalmap.entrySet()) {
        if(curr.equals(me.getKey())){
          List<Integer> arr = me.getValue();
          int size = arr.size();
          for(int lll = 0; lll<size; lll++){
            if(scope >= arr.get(lll)){
              flag1 = 1;
              break;
            }
          }
        }
        if(flag1 == 1){
          break;
        }
      }

      if(flag1 == 1){
        curr = gettoken();
        if(curr.equals("=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Jumbled variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else if(curr.equals("++")){
          curr = gettoken();
          if(curr.equals(";")){
            return true;
          }else{
            System.out.println(lno+1+": Not a valid statement...!");
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals("")){
                break;
              }
            }
            return false;
          }
        }else if(curr.equals("--")){
          curr = gettoken();
          if(curr.equals(";")){
            return true;
          }else{
            System.out.println(lno+1+": Not a valid statement...!");
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals("")){
                break;
              }
            }
            return false;
          }
        }else if(curr.equals("+=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else if(curr.equals("-=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else if(curr.equals("*=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else if(curr.equals("/=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else if(curr.equals("&=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else if(curr.equals("|=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else{
          return false;
        }
      }else{
        System.out.println(lno+1+": "+curr+"- Undeclared variable has been used..!");
        curr = gettoken();
        if(curr.equals("=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Jumbled variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else if(curr.equals("++")){
          curr = gettoken();
          if(curr.equals(";")){
            return true;
          }else{
            System.out.println(lno+1+": Not a valid statement...!");
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals("")){
                break;
              }
            }
            return false;
          }
        }else if(curr.equals("--")){
          curr = gettoken();
          if(curr.equals(";")){
            return true;
          }else{
            System.out.println(lno+1+": Not a valid statement...!");
            while(!curr.equals(";")){
              curr = gettoken();
              if(curr.equals("")){
                break;
              }
            }
            return false;
          }
        }else if(curr.equals("+=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else if(curr.equals("-=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else if(curr.equals("*=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else if(curr.equals("/=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else if(curr.equals("&=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else if(curr.equals("|=")){
          curr = gettoken();
          int tid = 1;
          int ther = 0;
          while(!curr.equals(";")){
            if((!( !(isconst(curr) ||checkvalidid(curr, scope)) || (tid == 1))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid variable or operator used...!");
              ther = 1;
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              break;
            }
          }
          if(ther == 1){
            return false;
          }else{
            return true;
          }
        }else{
          return false;
        }
      }
    }
    
  }

  public static boolean isop(String curr){
    if(curr.equals("==") || curr.equals("!=") || curr.equals(">") || curr.equals("<") || curr.equals("<=") || curr.equals(">=") || curr.equals("/") || curr.equals("*") || curr.equals("-") || curr.equals("+") || curr.equals("&") || curr.equals("|") || curr.equals("&&") || curr.equals("||")){
      return true;
    }
    return false;
  }

  public static void checkassignstmt2(String curr, int scope){
    if(checkkw(curr)){
      System.out.println(lno+1+": "+curr+" is not a valid identifier");
      System.exit(0);
    }

    if((curr.charAt(0)<'a' || curr.charAt(0)>'z') && ( curr.charAt(0)<'A' || curr.charAt(0)>'Z' ) ){
      System.out.println(lno+1+": "+curr+" is not a valid identifier");
      System.exit(0);
    }else{
      int flag1 = 0;
      for (Map.Entry<String, List<Integer>> me : globalmap.entrySet()) {
        if(curr.equals(me.getKey())){
          List<Integer> arr = me.getValue();
          int size = arr.size();
          for(int lll = 0; lll<size; lll++){
            if(scope >= arr.get(lll)){
              flag1 = 1;
              break;
            }
          }
        }
        if(flag1 == 1){
          break;
        }
      }

      if(flag1 == 1){
      }else{
        System.out.println(lno+1+": "+" "+curr+"- Undeclared variable has been used..!");
      }

      curr = gettoken();
        if(curr.equals("=")){
          curr = gettoken();
          int tid = 1;
          while(!curr.equals(")")){
            if(!((tid == 1) || !(isconst(curr) ||checkvalidid(curr, scope))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid update expression in for loop...!");
              System.exit(0);
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              System.out.println(lno+1+": Incorrect for format no ) parenthesis found");
              System.exit(0);
            }
          }
        }else if(curr.equals("++")){
          curr = gettoken();
          if(curr.equals(")")){
          }else{
            System.out.println(lno+1+": Not a valid update expression in for loop...!");
            System.exit(0);
          }
        }else if(curr.equals("--")){
          curr = gettoken();
          if(curr.equals(")")){
          }else{
            System.out.println(lno+1+": Not a valid update expression in for loop...!");
            System.exit(0);
          }
        }else if(curr.equals("+=")){
          curr = gettoken();
          int tid = 1;
          while(!curr.equals(")")){
            if(!((tid == 1) || !(isconst(curr) ||checkvalidid(curr, scope))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid update expression in for loop...!");
              System.exit(0);
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              System.out.println(lno+1+": Incorrect for format no ) parenthesis found");
              System.exit(0);
            }
          }
        }else if(curr.equals("-=")){
          curr = gettoken();
          int tid = 1;
          while(!curr.equals(")")){
            if(!((tid == 1) || !(isconst(curr) ||checkvalidid(curr, scope))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid update expression in for loop...!");
              System.exit(0);
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              System.out.println(lno+1+": Incorrect for format no ) parenthesis found");
              System.exit(0);
            }
          }
        }else if(curr.equals("*=")){
          curr = gettoken();
          int tid = 1;
          while(!curr.equals(")")){
            if(!((tid == 1) || !(isconst(curr) ||checkvalidid(curr, scope))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid update expression in for loop...!");
              System.exit(0);
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              System.out.println(lno+1+": Incorrect for format no ) parenthesis found");
              System.exit(0);
            }
          }
        }else if(curr.equals("/=")){
          curr = gettoken();
          int tid = 1;
          while(!curr.equals(")")){
            if(!((tid == 1) || !(isconst(curr) ||checkvalidid(curr, scope))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid update expression in for loop...!");
              System.exit(0);
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              System.out.println(lno+1+": Incorrect for format no ) parenthesis found");
              System.exit(0);
            }
          }
        }else if(curr.equals("&=")){
          curr = gettoken();
          int tid = 1;
          while(!curr.equals(")")){
            if(!((tid == 1) || !(isconst(curr) ||checkvalidid(curr, scope))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid update expression in for loop...!");
              System.exit(0);
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              System.out.println(lno+1+": Incorrect for format no ) parenthesis found");
              System.exit(0);
            }
          }
        }else if(curr.equals("|=")){
          curr = gettoken();
          int tid = 1;
          while(!curr.equals(")")){
            if(!((tid == 1) || !(isconst(curr) ||checkvalidid(curr, scope))) || (!((tid == 0) || (!(curr.equals("+")) && !(curr.equals("-")) && !(curr.equals("*")) && !(curr.equals("/")) && !(curr.equals("&")) && !(curr.equals("|")))))){
              System.out.println(lno+1+": Not a valid update expression in for loop...!");
              System.exit(0);
            }
            tid = 1-tid;
            curr = gettoken();
            if(curr.equals("")){
              System.out.println(lno+1+": Incorrect for format no ) parenthesis found");
              System.exit(0);
            }
          }
        }else{
          System.out.println(lno+1+": Not a valid update expression in for loop...!");
          System.exit(0);
        }
    }
  }

  public static boolean checkboolexpr(int scope, String stop){
    int lll = lno;
    String curr = gettoken();
    if(curr.equals(")") && stop.equals("{")){
      checkcp(curr);
      return false;
    }
    if(curr.equals(";")){
      return false;
    }
    int state = 0;
    int cnt = 0;
    if(stop.equals("{")){
      cnt = 1;
    }else if(stop.equals(";")){
      cnt = 0;
    }
    while(!curr.equals(stop)){
      if(state == 0){
        if(curr.equals("(")){
          checkop(curr);
          cnt++;
        }else if(curr.equals("!")){
        }else if(checkvalidconstid2(curr, scope)){
          state = 1;
        }else{
          System.out.println(lno+1+": Incorrect Boolean expression..!");
          System.exit(0);
        }
      }else if(state == 1){
        if(curr.equals(")")){
          checkcp(curr);
          cnt--;
          if(cnt < 0){
            System.out.println(lno+1+": Incorrect Boolean expression, Imbalanced Paranthesis..!");
            System.exit(0);
          }
        }else if(curr.equals("++") || curr.equals("--")){
        }else if(isop(curr)){
          state = 0;
        }else if(curr.equals("(")){
          System.out.println(lno+1+": Incorrect Boolean expression, Incorrect parenthesis position in the expression..!");
          System.exit(0);
        }else{
          System.out.println(lno+1+": Incorrect Boolean expression..!");
          System.exit(0);
        }
      }
      curr = gettoken();
      if(curr.equals("")){
        System.out.println(lll+1+": Incorrect Boolean expression..!");
        System.exit(0);
      }
    }
    checkop(curr);
    if(cnt > 0){
      System.out.println(lll+1+": Missing Parenthesis in the boolean condition..!");
      System.exit(0);
    }
    if(state == 0){
      return false;
    }else{
      return true;
    }
  }
  
  public static void clearscopevar(int scope){
    for (Map.Entry<String, List<Integer>> me : globalmap.entrySet()) {
      List<Integer> arr = me.getValue();
      int size = arr.size();
      for(int lll = 0; lll<size; lll++){
        if(scope+1 == arr.get(lll)){
          me.getValue().remove(lll);
        }
      }
    }
  }

  public static void checkelse(int scope){
    int index = i;
    if(lno >= lsz)return;
    String l = lines[lno];
    int lineno = lno;
    String curr = "";
    int n = l.length();

    if(index == n){
      lineno++;
      index = 0;
      if(lineno >= lsz)return;
      l = lines[lineno];
      n = l.length();
    }

    while(l.charAt(index) == ' '){
      index++;
      if(index == n){
        lineno++;
        index = 0;
        if(lineno >= lsz)return;
        l = lines[lineno];
        n = l.length();
      }
    }
    while(index < n && checkalphanum(l.charAt(index))){
      curr += l.charAt(index);
      index++;
    }

    if(curr.equals("else")){
      curr = gettoken();
      curr = gettoken();
      if(curr.equals("{")){
        checkop(curr);
        checkstatements(scope+1);
        clearscopevar(scope);
        checkstatements(scope);
      }else{
        checkstatements(scope);
        
      }
    }else{
      // while(l.charAt(index) == ' '){
      //   index++;
      //   if(index == n){
      //     lineno++;
      //     index = 0;
      //     if(lineno >= lsz)return;
      //     l = lines[lineno];
      //     n = l.length();
      //   }
      // }
      // if(index < n && l.charAt(index) == '{'){
      //   curr = gettoken();
      //   System.out.println(lno+1+": Else Wrongly written");
      // }else{return;}
      return;
      
    }
  }
  public static void checkif(int scope){
    String curr = gettoken();
    if( curr.equals("(") ){
      checkop(curr);
      if(checkboolexpr(scope, "{")){
      }else{
        System.out.println( lno+1+": if condition missing..!" );
      }    
        checkstatements(scope+1);
        clearscopevar(scope);
        checkelse(scope);
    }else{
      System.out.println( lno+1+": Missing ( bracket for if format..!" );
      checkop("(");
      if(checkboolexpr(scope, "{")){
      }else{
        System.out.println( lno+1+": if condition missing..!" );
      }     
        checkstatements(scope+1);
        clearscopevar(scope);
        checkelse(scope);
    }

    
  }

  public static void checkstatements2(String curr, int scope){
    //String l = lines[lno];
    //int n = l.length();

    if(lno >= lsz){
      return;
    }

    if(curr.equals("")){
      return;
    }else if( curr.equals(";") ){
      checkstatements(scope);
    }else if( curr.equals("{") ){
      checkop(curr);
      checkstatements(scope);
    }else if( curr.equals("(") ){
      checkop(curr);
      checkstatements(scope);
    }else if( curr.equals(")") ){
      checkcp(curr);
      checkstatements(scope);
    }else if( curr.equals("}") ){
      checkcp(curr);
      return;
    }else if( curr.equals("for") ){
      checkfor(scope+1);
      checkstatements(scope);
    }else if( curr.equals("while") ){
      checkwhile(scope);
      checkstatements(scope);
    }else if( curr.equals("do") ){
      //checkdo(scope+1);
      //checkstatements(scope);
    }else if( curr.equals("if") ){
      checkif(scope);
      checkstatements(scope);
    }else if(checkdeclarations(curr, scope)){
      checkstatements(scope);
    }else if(curr.equals("else")){
      System.out.println(lno+1+": Statement started with else. Missing if..!");
      curr = gettoken();
      if(curr.equals("{")){
        checkop(curr);
        checkstatements(scope+1);
        clearscopevar(scope);
        return;
      }else{
        checkstatements(scope);
      }

    }else if(isid(curr)){
      checkassignstmt(curr, scope);
      checkstatements(scope);
    }else{
      System.out.println(lno+1+": Unknown statement..!");

      while(!curr.equals(";")){
        curr = gettoken();

        if(curr.equals("")){
          break;
        }
      }
      checkstatements(scope);
    }
    
  }

  public static void checkstatements(int scope){
    //String l = lines[lno];
    //int n = l.length();
    if(lno >= lsz){
      return;
    }

    String curr = gettoken();    
    
    if(curr.equals("")){
      return;
    }else if( curr.equals(";") ){
      checkstatements(scope);
    }else if( curr.equals("{") ){
      checkop(curr);
      checkstatements(scope);
    }else if( curr.equals("(") ){
      checkop(curr);
      checkstatements(scope);
    }else if( curr.equals(")") ){
      checkcp(curr);
      checkstatements(scope);
    }else if( curr.equals("}") ){
      checkcp(curr);
      return;
    }else if( curr.equals("for") ){
      checkfor(scope+1);
      checkstatements(scope);
    }else if( curr.equals("while") ){
      checkwhile(scope);
      checkstatements(scope);
    }else if( curr.equals("do") ){
      //checkdo(scope+1);
      //checkstatements(scope);
    }else if( curr.equals("if") ){
      checkif(scope);
      checkstatements(scope);
    }else if(checkdeclarations(curr, scope)){
      checkstatements(scope);
    }else if(curr.equals("else")){
      System.out.println(lno+1+": Statement started with else. Missing if..!");
      curr = gettoken();
      if(curr.equals("{")){
        checkop(curr);
        checkstatements(scope+1);
        clearscopevar(scope);
        return;
      }else{
        checkstatements(scope);
      }

    }else if(isid(curr)){
      checkassignstmt(curr, scope);
      checkstatements(scope);
    }else{
      System.out.println(lno+1+": Unknown statement..!");

      while(!curr.equals(";")){
        curr = gettoken();

        if(curr.equals("")){
          break;
        }
      }
      checkstatements(scope);
    }
    
  }
  
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader("program.cpp"));
    StringBuilder sb = new StringBuilder();
    String line = br.readLine();
    while (line != null) {
      sb.append(line).append("\n");
      line = br.readLine();
    }
    String cppProgram = sb.toString();
    lines = cppProgram.split("\n");

    boolean header = true;
    boolean program = true;
    lsz = lines.length;

    
    for(int i =0; i<lsz; i++){
      String l = lines[ i ];
      int commentIndex = l.indexOf("//");
      if (commentIndex != -1) {
        lines[i] = l.substring(0, commentIndex);
      }
    }

    int linesno = 0;
    while(linesno < lsz){
      String l = lines[ linesno ];
      int commentIndex = l.indexOf("/*");
      int commentIndex2 = l.indexOf("*/");
      if(commentIndex != -1 && commentIndex2 == -1){
        lines[linesno] = l.substring(0, commentIndex);
        commentIndex = -1;
        linesno++;
        if(linesno >= lsz)break;

        l = lines[ linesno ];
        commentIndex = l.indexOf("*/");
        while(commentIndex == -1 && linesno < lsz){
          lines[linesno] = "";
          linesno++;
          if(linesno >= lsz)break;
          l = lines[ linesno ];
          commentIndex = l.indexOf("*/");
        }
        if(linesno >= lsz)break;
        lines[linesno] = l.substring(commentIndex+2);
      }else if(commentIndex != -1 && commentIndex2 != -1){
        String l2 = l.substring(0, commentIndex) + l.substring(commentIndex2+2);
        lines[linesno] = l2;
      }
      linesno++;
    }


    lno = 0;
    while(lno < lsz){
      String l = lines[ lno ];
      
      i = 0;
      
      if(l != ""){
        skipwhitespace();
        if(header && l.charAt(i) == '#'){
          checkheader(l);
        }else if(header && l.charAt(i) == 'u'){
          String curr = gettoken();
          if(curr.equals("using")){
            curr = gettoken();
            if(curr.equals("namespace")){
              curr = gettoken();
              if(curr.equals("std")){
                curr = gettoken();
                if(curr.equals(";")){
                }else{
                  System.out.println(lno+1+": Incorrect header");
                  System.exit(0);
                }
              }else{
                System.out.println(lno+1+": Incorrect header");
                System.exit(0);
              }
            }else{
              System.out.println(lno+1+": Incorrect header");
              System.exit(0);
            }
          }else{
            System.out.println(lno+1+": Incorrect header");
            System.exit(0);
          }
        }else{
          if(!header && l.charAt(i) == '#'){
            System.out.println(lno+1+": Header in b/w code");
          }else{
            header = false;
            if(program == true){
              program = false;
              if(match(l, "void")){
                if(l.charAt(i) == ' '){
                  skipwhitespace();
                  if(match(l, "main")){
                    // System.out.println(lsz);
                    while(lno < lsz)checkstatements(1);
                    if(!charStack.empty()){
                      System.out.println(lno+1+": Imbalanced parenthesis");
                    }
                    System.out.println("Program checked..!!");
                    
                  }else{
                    System.out.println(lno+1+": No main function found");
                    System.exit(0);
                  }
                }else{
                  System.out.println(lno+1+": No return type found");
                  System.exit(0);
                }
              }else{
                System.out.println(lno+1+": No return type found");
                System.exit(0);
              }
            }else{
              System.out.println(lno+1+": No main function found");
              System.exit(0);
            }
          }
        }
      }
      lno++;
    }
    br.close();

    if(program == true){
      System.out.println(lno+1+": No main function found");
      System.exit(0);
    }
  }
}
  