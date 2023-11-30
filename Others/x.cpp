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
      System.out.println(lno+1 + " "+i);
      System.out.println(curr);
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
        System.out.println(lno+1 + " "+i);
        System.out.println(curr);
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
      System.out.println(lno+1 + " "+i);
      System.out.println(curr);
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
      System.out.println(lno+1 + " "+i);
      System.out.println(curr);
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

    System.out.println(lno+1 + " "+i);
    System.out.println(curr);
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
    System.out.println(lno+": pushed parenthesis");
  }

  public static void checkcp(String curr){
    
    if(curr.equals("}")  && (!charStack.empty() && charStack.peek() == '{') ){
      charStack.pop();
      System.out.println(lno+": poped parenthesis");
    }else if(curr.equals(")")  && (!charStack.empty() && charStack.peek() == '(') ){
      charStack.pop();
      System.out.println(lno+": poped parenthesis");
    }else{
      charStack.pop();
      System.out.println(lno+": Unmatched parenthesis");
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
    System.out.println(scope);
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
        System.out.println(lno+1+": Not a valid statement..!");
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
        return;
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
      System.out.println(lno+1+": Not a valid statement..!");
    }

  }

  public static boolean checkdeclarations(String curr, int scope){

    if(curr.equals("string") ){
      if(!checkid(scope)){
        System.out.println(lno+1+": Not a valid statement..!");
      }
      curr = gettoken();
      if(curr.equals(";")){
        return true;
      }else if(curr.equals("=")){

        curr = gettoken();
        if(curr.equals(";")){
          System.out.println(lno+1+": Not a valid statement..!");
        }else if(curr.charAt(0) == '\"'){

          curr = gettoken();
          if(!curr.equals(";")){
            System.out.println(lno+1+": Not a valid statement..! ");
            while(!curr.equals(";")){
              curr = gettoken();
            }
          }
        }else if(curr.charAt(0) == '\''){
          System.out.println(lno+1+": Cannot assign char to a string variable..! ");
          curr = gettoken();
          if(!curr.equals(";")){
            while(!curr.equals(";")){
              curr = gettoken();
            }
          }
        }else if(isconst(curr)){
          System.out.println(lno+1+": String cannot be equated to constant number..! ");
        }else if(isid(curr)){
          if(globalmap.get(curr) == null){
            System.out.println(lno+1+": "+" "+curr+" is not a declared variable to equate..!");
          }
          curr = gettoken();
          if(!curr.equals(";")){
            System.out.println(lno+1+": Not a valid statement..! ");
            while(!curr.equals(";")){
              curr = gettoken();
            }
          }
        }else{
          System.out.println(lno+1+": "+" "+curr+" is not a valid variable or constant to equate..!");
          curr = gettoken();
          if(!curr.equals(";")){
            System.out.println(lno+1+": Not a valid statement..! ");
            while(!curr.equals(";")){
              curr = gettoken();
            }
          }
        }

      }else{
        System.out.println(lno+1+": Not a valid statement..!");
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
        System.out.println(lno+1+": Not a valid statement..!");
      }
      curr = gettoken();
      if(curr.equals(";")){
        return true;
      }else if(curr.equals("=")){
  
        curr = gettoken();
        if(curr.equals(";")){
          System.out.println(lno+1+": Not a valid statement..!");
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
        System.out.println(lno+1+": Not a valid statement..!");
      }
      return true;
    }
    return false;

    // else if(curr == "short"){
    //   return true;
    // }else if(curr == "signed"){
    //   return true;
    // }else if(curr == "unsigned"){
    //   return true;
    // }
  }

  public static boolean checkboolexpr(String stmt, int scope){

    if(stmt.equals("true") || stmt.equals("false")  ){
      return true;
    }
    if(stmt.equals("")){
      return false;
    }
    int[] ind = new int[6];
    ind[0] = stmt.indexOf(">");
    ind[1] = stmt.indexOf("<");
    ind[2] = stmt.indexOf(">=");
    ind[3] = stmt.indexOf("<=");
    ind[4] = stmt.indexOf("==");
    ind[5] = stmt.indexOf("!=");
    if(ind[0] == ind[2]){
      ind[2] = -1;
    }
    if(ind[1] == ind[3]){
      ind[3] = -1;
    }
    int ther = 0;

    for(int i =0; i<6; i++){
      if(ther == 0 && (ind[i] != -1)){
        ther = 1;
      }else if(ther == 1 && (ind[i] != -1)){
        return false;
      }
    }

    return true;
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
    System.out.println("Checking else");  
    int index = i;
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
        return;
      }else{

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
      System.out.println("Checking no else");  
      return;
      
    }
  }
  public static void checkif(int scope){
    String curr = gettoken();
    if( curr.equals("(") ){
      checkop(curr);

      curr = gettoken();
      String totcurr = "";
      while(!curr.equals(")")){
        totcurr += curr;
        curr = gettoken();
        if(curr.equals(""))break;
      }

      if(!curr.equals(")")){
        System.out.println(lno+1+": Wrong if format..!");
        return;
      }

      if(checkboolexpr(totcurr, scope)){
      }else if(totcurr.equals("")){
        System.out.println(lno+1+": Missing boolean expression..!");
      }else{
        System.out.println(lno+1+": Incorrect boolean expression..!");
      }
      checkcp(curr);

      curr = gettoken();
      if(curr.equals("{")){
        checkop(curr);
        checkstatements(scope+1);
        clearscopevar(scope);
        checkelse(scope);
        return;
      }else{
        
      }
      

    }else{
      System.out.println( lno+1+": Missing ( bracket for if format..!" );
      while(!curr.equals("{") && !curr.equals(";") && !checkkw(curr) ){
        curr = gettoken();
        if(curr.equals("")){
          return;
        }
      }
      if(checkkw(curr)){
        checkstatements(scope);
      }else{
        checkstatements(scope+1);
        clearscopevar(scope);
      }
      // if(checkboolexpr(totcurr, scope)){

      // }else{
      //   System.out.println(lno+1+": Missing boolean expression..!");
      // }
    }
  }

  public static void checkstatements(int scope){
    String l = lines[lno];
    int n = l.length();

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
      //checkfor(scope+1);
      //checkstatements(scope);
    }else if( curr.equals("while") ){
      //checkwhile(scope+1);
      //checkstatements(scope);
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
        for (Map.Entry<String, List<Integer>> me : globalmap.entrySet()) {
          List<Integer> arr = me.getValue();
          int size = arr.size();
          for(int lll = 0; lll<size; lll++){
            if(scope+1 == arr.get(lll)){
              me.getValue().remove(lll);
            }
          }
        }
        return;
      }else{

      }

    }else if(isid(curr)){

      // System.out.println(lno+1+": Unknown statement..!");

      // while(!curr.equals(";")){
      //   curr = gettoken();
      // }
      checkstatements(scope);
    }
    
  }
  
  public static void main(String[] args) throws IOException {
    // Read the C++ program into a string
    BufferedReader br = new BufferedReader(new FileReader("program.cpp"));
    StringBuilder sb = new StringBuilder();
    String line = br.readLine();
    while (line != null) {
      sb.append(line).append("\n");
      line = br.readLine();
    }
    String cppProgram = sb.toString();
    
    // Split the program into individual lines
    lines = cppProgram.split("\n");
    
    // Parse each line
    boolean header = true;
    boolean program = true;
    lsz = lines.length;
    lno = 0;
    while(lno < lsz){
      String l = lines[ lno ];
      // Remove comments
      int commentIndex = l.indexOf("//");
      if (commentIndex != -1) {
        l = l.substring(0, commentIndex);
      }
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
                }
              }else{
                System.out.println(lno+1+": Incorrect header");
              }
            }else{
              System.out.println(lno+1+": Incorrect header");
            }
          }else{
            System.out.println(lno+1+": Incorrect header");
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
                    System.out.println(lsz);
                    checkstatements(1);
                    if(!charStack.empty()){
                      System.out.println(lno+1+": Imbalanced parenthesis");
                    }
                    System.out.println("Program checked..!!");
                  }else{
                    System.out.println(lno+1+": No main function found");
                  }
                }else{
                  System.out.println(lno+1+": No return type found");
                }
              }else{
                System.out.println(lno+1+": No return type found");
              }
            }else{

            }
          }
        }
      }
      lno++;
    }
    br.close();
  }

 
}



  