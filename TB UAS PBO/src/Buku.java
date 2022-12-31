import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

//class Buku
public class Buku implements Database // class Buku yang mengimplementasikan interface Database
{
    // koneksi database
    Connection conn;
    String link = "jdbc:mysql://localhost:3306/pinjam_buku";
    Scanner input = new Scanner(System.in);

    String nama_peminjam, judul, id_buku;
    Integer jumlah;
    boolean kosongView = true;

    // constructor
    public Buku() {
    }

    // constructor
    public Buku(Integer a) {
        System.out.println("constructor pada class Buku dengan parameter integer");
    }

    // constructor
    public Buku(String judul, String id_buku) {
        this.judul = judul;
        this.id_buku = id_buku;
        System.out.println("Buku disimpan dengan judul \n" + this.judul + " dengan id " + this.id_buku);
    }

    public void methodKosong() {
    }

    // method implementasi dari interface
    @Override
    public void view() throws SQLException {
        // mengakses data yang berada didatabase pinjam_buku tabel buku
        String sql = "SELECT * FROM buku";
        // link = "jdbc:mysql://localhost:3306/pinjam_buku";
        conn = DriverManager.getConnection(link, "root", "");
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(sql);

        // percabangan while
        while (result.next())// akan terus dilakukan perulangan ke terminal jika data pd database masih ada
        {
            kosongView = false;
            // blok kodingan untuk mengakses data ditabel buku dan mencetaknya di terminal
            System.out.println("\nJudul\t : ");
            System.out.println(result.getString("judul"));// memperoleh nilainya menggunakan getstring pada kolom judul
            System.out.println("\nID Buku\t : ");
            System.out.println(result.getString("id_buku"));// memperoleh nilainya menggunakan getstring pada kolom
                                                            // id_buku
            System.out.println("\nJumlah\t : ");
            System.out.println(result.getInt("jumlah"));// memperoleh nilainya menggunakan getstring pada kolom
                                                        // jumlah
        }

        if (kosongView) {
            System.out.println("Data Buku Tidak Ditemukan!");
        }
        statement.close();
    }

    // method implementasi dari interface
    @Override
    public void update() throws SQLException {
        // try
        try {
            view();
            Integer pil;

            // percabangan if, akan dicek apakah kosongView = true
            if (kosongView)// jika iya, maka dicetak
            {
                System.out.println("Data Kosong, Tidak dapat mengubah Data");
            } else// jika tidak, maka dijalankan program dibawah
            {
                String text = "\nUbah data buku";
                System.out.println(text.toUpperCase());// akan diubah menjadi kapital semua
                System.out.println("ID buku yng ingin diubah : ");
                String ubah = input.nextLine();

                // mengakses database pinjam_buku tabel buku
                String sql = "SELECT * FROM buku WHERE id_buku='" + ubah + "'";
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql);

                // percabangan if
                if (result.next())// apakah result.next() = true, jika iya maka akan dijalankan program dibawah
                {
                    System.out.println("Data yang ingin diubah\n1.Judul\n2.Jumlah");
                    System.out.println("Pilihan (1/2) : ");
                    pil = input.nextInt();// inputan user
                    input.nextLine();

                    // percabangan switch case
                    switch (pil) {
                        case 1:
                            System.out.println("Judul [" + result.getString("judul") + "]\t: ");
                            String ganti1 = input.nextLine();
                            // update data pinjam_buku tabel buku
                            sql = "UPDATE buku SET judul = '" + ganti1 + "' WHERE id_buku = '" + ubah + "'";
                            if (statement.executeUpdate(sql) > 0) {
                                System.out.println("Judul berhasil diperbarui (ID Buku " + ubah + ")");
                            }
                            break;

                        case 2:
                            System.out.println("Jumlah [" + result.getInt("jumlah") + "]\t: ");
                            Integer ganti2 = input.nextInt();
                            // update data pinjam_buku tabel buku
                            sql = "UPDATE buku SET jumlah = '" + ganti2 + "' WHERE id_buku = '" + ubah + "'";
                            input.nextLine();
                            if (statement.executeUpdate(sql) > 0) {
                                System.out.println("Jumlah berhasil diperbarui (ID Buku " + ubah + ")");
                            }
                            break;

                        default:
                            System.out.println("Mohon inputkan angka yang benar (1/2)!");
                            break;
                    }
                } else {
                    System.out.println("ID Buku tidak ditemukan");
                }
            }
        }
        // exception SQL
        catch (SQLException e) {
            System.err.println("Kesalahan update data");
        }
        // exception input tidak sesuai dengan jenis data
        catch (InputMismatchException e) {
            System.err.println("Inputan harus dalam bentuk angka!");
        }
    }

    // ketiga method dibawah akan dialihkan ke class PinjamBuku dan BalikinBuku
    public void delete() throws SQLException {
    }

    public void save() throws SQLException {
    }

    public void search() throws SQLException {
    }
}