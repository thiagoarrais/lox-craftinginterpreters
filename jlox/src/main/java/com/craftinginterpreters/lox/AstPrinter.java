package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

// Creates an unambiguous, if ugly, string representation of AST nodes.
class AstPrinter implements Expr.Visitor<String> {                     
  String print(Expr expr) {                                            
    return expr.accept(this);                                          
  }                                                                    

  @Override                                                          
  public String visitBinaryExpr(Expr.Binary expr) {                  
    return parenthesize(expr.operator.lexeme, expr.left, expr.right);
  }

  @Override                                             
  public String visitCallExpr(Expr.Call expr) {         
    List<Expr> call = new ArrayList<Expr>();
    call.add(expr.callee);
    call.addAll(expr.arguments);

    return parenthesize("call", call);
  }

  @Override                                        
  public String visitGetExpr(Expr.Get expr) {      
    return parenthesize("get " + expr.name, expr.object);
  }

  @Override                                                            
  public String visitSetExpr(Expr.Set expr) {                          
    return parenthesize("set " + expr.name, expr.object, expr.value);
  }

  @Override                                           
  public String visitSuperExpr(Expr.Super expr) {     
    return "(super " + expr.method + ")";
  }

  @Override                                    
  public String visitThisExpr(Expr.This expr) {
    return "this"; 
  }

  @Override                                                          
  public String visitGroupingExpr(Expr.Grouping expr) {              
    return parenthesize("group", expr.expression);                   
  }                                                                  

  @Override                                                          
  public String visitLiteralExpr(Expr.Literal expr) {                
    if (expr.value == null) return "nil";                            
    return expr.value.toString();                                    
  }                                                                  

  @Override                                                          
  public String visitLogicalExpr(Expr.Logical expr) {                    
    return parenthesize(expr.operator.lexeme, expr.left, expr.right);
  }

  @Override                                                          
  public String visitUnaryExpr(Expr.Unary expr) {                    
    return parenthesize(expr.operator.lexeme, expr.right);           
  }

  @Override                                                          
  public String visitVariableExpr(Expr.Variable expr) {                    
    return "(variable " + expr.name + ")";
  }

  @Override                                        
  public String visitAssignExpr(Expr.Assign expr) {
    return parenthesize("assign " + expr.name.lexeme, expr.value);                                  
  }

  /*
  @Override                                              
  public String visitExpressionStmt(Stmt.Expression stmt) {
    evaluate(stmt.expression);                           
    return null; 
  }

  @Override                                    
  public String visitPrintStmt(Stmt.Print stmt) {
    Object value = evaluate(stmt.expression);  
    System.out.println(stringify(value));      
    return null;                               
  }

  @Override                                     
  public String visitVarStmt(Stmt.Var stmt) {     
    Object value = null;                        
    if (stmt.initializer != null) {             
      value = evaluate(stmt.initializer);       
    }

    environment.define(stmt.name.lexeme, value);
    return null;                                
  }
  */

  private String parenthesize(String name, Expr... exprs) {
    return parenthesize(name, Arrays.asList(exprs));
  }                                                        

  private String parenthesize(String name, List<Expr> exprs) {
    StringBuilder builder = new StringBuilder();

    builder.append("(").append(name);                      
    for (Expr expr : exprs) {                              
      builder.append(" ");                                 
      builder.append(expr.accept(this));                   
    }                                                      
    builder.append(")");                                   

    return builder.toString();                             
  }

  public static void main(String[] args) {                 
    Expr expression = new Expr.Binary(                     
        new Expr.Unary(                                    
            new Token(TokenType.MINUS, "-", null, 1),      
            new Expr.Literal(123)),                        
        new Token(TokenType.STAR, "*", null, 1),           
        new Expr.Grouping(                                 
            new Expr.Literal(45.67)));

    System.out.println(new AstPrinter().print(expression));
  }
}    
