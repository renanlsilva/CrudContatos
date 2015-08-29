/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.crudcontatos;

/**
 *
 * @author renan.lsilva3
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Renan
 */
public class ContatosApp {

  /**
   * @param args the command line arguments
   */
  
  private Connection obterConexao() throws SQLException, ClassNotFoundException {
    Connection conn = null;
    // Passo 1: Registrar driver JDBC.
    Class.forName("org.apache.derby.jdbc.ClientDataSource");

    // Passo 2: Abrir a conexão
    conn = DriverManager.getConnection("jdbc:derby://localhost:1527/agendadb;SecurityMechanism=3",
            "app", // usuario
            "app"); // senha
    return conn;
  }

  public void listarPessoas() {
    Statement stmt = null;
    Connection conn = null;

    String sql = "SELECT ID_PESSOA, NM_PESSOA, DT_NASCIMENTO, VL_TELEFONE, VL_EMAIL FROM TB_PESSOA";
    try {
      conn = obterConexao();
      stmt = conn.createStatement();
      ResultSet resultados = stmt.executeQuery(sql);

      DateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy");

      while (resultados.next()) {
        Long id = resultados.getLong("ID_PESSOA");
        String nome = resultados.getString("NM_PESSOA");
        Date dataNasc = resultados.getDate("DT_NASCIMENTO");
        String email = resultados.getString("VL_EMAIL");
        String telefone = resultados.getString("VL_TELEFONE");
        System.out.println(String.valueOf(id) + ", " + nome + ", " + formatadorData.format(dataNasc) + ", " + email + ", " + telefone);
      }

    } catch (SQLException ex) {
      Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException ex) {
          Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException ex) {
          Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  public void incluirPessoa() {
    PreparedStatement stmt = null;
    Connection conn = null;

    String nome;
    Date dataNasc;
    String email;
    String telefone;

    // ENTRADA DE DADOS
    Scanner entrada = new Scanner(System.in);
    System.out.print("Digite o nome da pessoa: ");
    nome = entrada.nextLine();

    System.out.print("Digite a data de nascimento no formato dd/mm/aaaa: ");
    String strDataNasc = entrada.nextLine();
    DateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy");
    try {
      dataNasc = formatadorData.parse(strDataNasc);
    } catch (ParseException ex) {
      Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
      dataNasc = new Date();
    }
    System.out.print("Digite o telefone no formato 99 99999-9999: ");
    telefone = entrada.nextLine();

    System.out.print("Digite o e-mail: ");
    email = entrada.nextLine();

    String sql = "INSERT INTO TB_PESSOA (NM_PESSOA, DT_NASCIMENTO, VL_TELEFONE, VL_EMAIL) VALUES (?, ?, ?, ?)";
    try {
      conn = obterConexao();
      stmt = conn.prepareStatement(sql);
      stmt.setString(1, nome);
      stmt.setDate(2, new java.sql.Date(dataNasc.getTime()));
      stmt.setString(3, telefone);
      stmt.setString(4, email);
      //stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
      stmt.executeUpdate();
      System.out.println("Registro incluido com sucesso.");

    } catch (SQLException ex) {
      Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException ex) {
          Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException ex) {
          Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }
  
  public void alterarPessoa(){
    PreparedStatement stmt = null;
    Connection conn = null;
    int Id;  
    String nome;
    Date dataNasc;
    String email;
    String telefone;
    String sql;

    // ENTRADA DE DADOS
    
    listarPessoas();
    
    Scanner entrada = new Scanner(System.in);
    System.out.print("Digite o ID da pessoa que deseja alterar: ");
    Id = entrada.nextInt();
       

    // ENTRADA DE DADOS
   
    System.out.print("Digite o nome da pessoa: ");
    nome = entrada.next();

    System.out.print("Digite a data de nascimento no formato dd/mm/aaaa: ");
    String strDataNasc = entrada.next();
    DateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy");
    try {
      dataNasc = formatadorData.parse(strDataNasc);
    } catch (ParseException ex) {
      Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
      dataNasc = new Date();
    }
    System.out.print("Digite o telefone no formato 99999-9999: ");
    telefone = entrada.next();

    System.out.print("Digite o e-mail: ");
    email = entrada.next();

    sql = ("UPDATE TB_PESSOA SET NM_PESSOA =?, DT_NASCIMENTO =?, VL_TELEFONE =?, VL_EMAIL =? WHERE ID_PESSOA="+Id+"");
    try {
      conn = obterConexao();
      stmt = conn.prepareStatement(sql);
      stmt.setString(1, nome);
      stmt.setDate(2, new java.sql.Date(dataNasc.getTime()));
      stmt.setString(3, telefone);
      stmt.setString(4, email);
      stmt.executeUpdate();
      System.out.println("Registro alterado com sucesso.");

    } catch (SQLException ex) {
      Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException ex) {
          Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException ex) {
          Logger.getLogger(ContatosApp.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  public static void main(String[] args) {
    ContatosApp instancia = new ContatosApp();
    Scanner entrada = new Scanner(System.in);
    do {
      System.out.println("********** DIGITE UMA OPÇÃO **********");
      System.out.println("(1) Listar agenda");
      System.out.println("(2) Incluir registro");
      System.out.println("(3) Alterar registro");
      System.out.println("(9) SAIR");
      System.out.print("Opção: ");
      int opcao = entrada.nextInt();

      if (opcao == 1) {
        instancia.listarPessoas();
      } else if (opcao == 2) {
        instancia.incluirPessoa();     
   
      }else if (opcao == 3) {
        instancia.alterarPessoa();
      } else if (opcao == 9) {
        System.exit(0);
      } else {
        System.out.println("OPÇÃO INVÁLIDA.");
      }

    } while (true);
  }
}
