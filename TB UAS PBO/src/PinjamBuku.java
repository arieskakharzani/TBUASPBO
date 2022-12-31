import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

//class PinjamBuku akan mewariskan semua method yang ada di class Buku
public class PinjamBuku extends Buku {
    // koneksi database
    Connection conn;
    String link = "jdbc:mysql://localhost:3306/pinjam_buku";

    Scanner input = new Scanner(System.in);
    boolean bukuTersedia = false, peminjamTersedia = false;
    Integer jumlahBuku, jumlahAwal, jumlahAkhir, jumlahPinjam;
    String nama_peminjam, id_peminjam;

    // method method berikut berfungsi untuk input data dan disimpan di variabel
    public void judul_buku() {
        System.out.println("Judul Buku\t: ");
        this.judul = input.nextLine();// input judul dan menyimpannya ke variabel
    }

    public void id_buku() {
        System.out.println("ID Buku\t: ");
        this.id_buku = input.nextLine();
    }

    public void jumlah() {
        System.out.println("Jumlah Buku yang Dipinjam\t: ");
        this.jumlah = input.nextInt();
    }

    public void nama_peminjam() {
        System.out.println("Nama Peminjam\t: ");
        this.nama_peminjam = input.nextLine();
    }

    public void id_peminjam() {
        System.out.println("ID Peminjam\t: ");
        this.id_peminjam = input.nextLine();
    }

    public void cekID_buku() throws SQLException {// method utk mengecek apakah buku tsb ada di database
        // mengakses data pinjam_buku tabel buku
        conn = DriverManager.getConnection(link, "root", "");
        Statement statement = conn.createStatement();
        String sql = "SELECT * FROM buku WHERE id_buku LIKE '" + this.id_buku + "'";
        ResultSet result = statement.executeQuery(sql);

        if (result.next()) {// jika ada nilainya di database maka akan muncul pesan spt dibawah
            System.out.println("ID buku " + this.id_buku + " pernah diinputkan sebelumnya");
            this.jumlahAwal = result.getInt("jumlahBuku");// diambil data jumlah buku
            this.judul = result.getString("judul");// diambil data judul buku dri database
            bukuTersedia = true;
        }

    }

    public void bukuTersedia() throws SQLException {// jika buku tersedia maka akan dijalankan method bukuTersedia
        // mengakses data pinjam_buku tabel buku
        String sql = "SELECT * FROM buku WHERE id_buku LIKE '" + this.id_buku + "'";
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(sql);

        if (result.next()) {// utk eksekusi dri sqlnya

            // update data pinjam_buku tabel buku
            System.out.println("ID buku " + this.id_buku + " berubah " + this.jumlahAwal + " menjadi "
                    + this.jumlahAkhir + " eksemplar");
            sql = "UPDATE buku SET jumlah = " + this.jumlahAkhir + " WHERE id_buku ='" + this.id_buku + "'";
            // jika buku yg ditambahkan dg judul yg sama maka tidak akan menambahkan data
            // baru, hanya stoknya atau jumlahnya saja
            if (statement.executeUpdate(sql) > 0) {
                System.out.println("Berhasil memperbarui jumlah");
            }
        }
    }

    public void peminjamTersedia() throws SQLException {
        // input data ke database pinjam_buku tabel meminjam
        conn = DriverManager.getConnection(link, "root", "");
        String sql = "INSERT INTO meminjam (nama_peminjam, judul, id_buku, jumlah, id_peminjam)";
        Statement statement = conn.createStatement();
        statement.execute(sql);
    }

    public void cekID_peminjam() throws SQLException {// method utk mengecek apakah id_peminjam tsb ada di database
        // mengakses database pinjam_buku tabel meminjam
        conn = DriverManager.getConnection(link, "root", "");
        Statement statement = conn.createStatement();
        String sql = "SELECT * FROM meminjam WHERE id_peminjam LIKE '" + this.id_peminjam + "'";
        ResultSet result = statement.executeQuery(sql);

        if (result.next()) {// jika ada nilainya di database maka akan muncul pesan spt dibawah
            System.out.println("ID peminjam " + this.id_peminjam + " pernah diinputkan sebelumnya");
            // menyimpan nilai dari data pinjam_buku tabel meminjam ke variabel
            this.nama_peminjam = result.getString("nama_peminjam");// diambil data jumlah buku pada database
            peminjamTersedia = true;
        }

    }

    public void bukuBaru() throws SQLException {// jika method ini dijalankan akan muncul pesan yg ada pd constructor di
                                                // class Buku
        Buku buku = new Buku(this.judul, this.id_buku);
        buku.methodKosong();

        conn = DriverManager.getConnection(link, "root", "");
        Statement statement = conn.createStatement();
        // input data ke database pinjam_buku tabel buku
        String sql = "INSERT INTO buku (judul, id_buku, jumlah) VALUES ('" + this.judul + "', '" + this.id_buku + "','"
                + this.jumlah + "'";
        statement.execute(sql);
        System.out.println("Berhasil input data");
    }

    public void peminjamBaru() throws SQLException {
        Peminjam peminjam = new Peminjam(this.nama_peminjam, this.id_peminjam);
        peminjam.methodKosong();

        conn = DriverManager.getConnection(link, "root", "");
        Statement statement = conn.createStatement();
        // input data ke database pinjam_buku tabel meminjam
        String sql = "INSERT INTO meminjam (nama_peminjam, judul, id_buku, jumlah, id_peminjam) VALUES ('"
                + this.nama_peminjam + "', '" + this.judul + "','" + this.id_buku + "','" + this.jumlah + "', '"
                + this.id_peminjam + "'";
        statement.execute(sql);
    }

    // utk mengeksekusi method save
    @Override
    public void save() throws SQLException {
        try {// bila ada kesalahan, maka ada 2 yaitu exception SQL dan input mismatch
            nama_peminjam();
            id_peminjam();
            cekID_peminjam();
            judul_buku();
            id_buku();
            jumlah();
            cekID_buku();

            // percabangan if
            if (bukuTersedia) {
                if (peminjamTersedia) {
                    peminjamTersedia();
                    bukuTersedia();
                } else {
                    peminjamBaru();
                    bukuTersedia();
                }
            } else {
                // percabangan if
                if (peminjamTersedia) {
                    peminjamTersedia();
                    bukuBaru();
                } else {
                    peminjamBaru();
                    bukuBaru();
                }
            }
        }

        // exception SQL
        catch (SQLException e) {
            System.err.println("Kesalahan dalam input data");
        }
        // exception input tidak sesuai dgn input data
        catch (InputMismatchException e) {
            System.out.println("Inputkan data dengan benar");
        }
    }
}