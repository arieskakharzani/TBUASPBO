import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

//class Buku, semua variabel dan method yg ada di class Buku akan diwariskan ke BalikinBuku
public class BalikinBuku extends Buku {
    // koneksi database
    Connection conn;
    String link = "jdbc:mysql://localhost:3306/pinuam_buku";
    // deklarasi variabel
    Scanner input = new Scanner(System.in);
    String nama_petugas, id_petugas, judul, id_buku;
    boolean bukuTersedia = false, bukuCukup = false, bukuPas = false;
    Integer jumlah, jumlahDikembalikan, jumlahAwal, jumlahAkhir;

    // method method berikut berfungsi untuk input data dan disimpan di variabel
    public void nama_petugas() {
        System.out.println("    Nama petugas\t: ");
        this.nama_petugas = input.nextLine();
    }

    public void id_petugas() {
        System.out.println("    ID petugas\t: ");
        this.id_petugas = input.nextLine();
    }

    public void judul_buku() {
        System.out.println("    Judul buku\t: ");
        this.judul = input.nextLine();
    }

    public void id_buku() {
        System.out.println("    ID buku\t: ");
        this.id_buku = input.nextLine();
    }

    public void jumlahDikembalikan() {
        System.out.println("    Jumlah buku dikembalikan\t: ");
        this.jumlahDikembalikan = input.nextInt();
    }

    public void jumlah(Integer jumlah) {
        this.jumlahAwal = jumlah;
    }

    public void cekJumlah() {
        if (this.jumlahAwal > this.jumlahDikembalikan) {
            bukuCukup = true;
        } else if (this.jumlahAwal.equals(this.jumlahDikembalikan)) {
            bukuPas = true;
        } else {
            System.out.println("Jumlah buku tidak mencukupi rak");
        }
    }

    public void bukuBerkurang() throws SQLException {
        // logika matematika untuk memperoleh nilai dari jumlah akhir buku tersedia
        // jika ada buku yg dipinjam maka jumlah buku dirak akan berkurang
        this.jumlahAkhir = this.jumlahAwal - this.jumlahDikembalikan;
        // mengakses database
        conn = DriverManager.getConnection(link, "root", "");
        String sql = "SELECT * FROM buku WHERE id_buku='" + this.id_buku + "'";
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(sql);

        if (result.next()) // utk menjalankan eksekusi dri sqlnya
        {
            // update data pinjam_buku tabel buku
            sql = "UPDATE buku SET jumlah = " + this.jumlahAkhir + " WHERE id_buku ='" + this.id_buku + "'";
            // jika buku yg ditambahkan dg judul yg sama maka tidak akan menambahkan data
            // baru, hanya stoknya atau jumlahnya saja
            if (statement.executeUpdate(sql) > 0) {
                System.out.println("Berhasil memperbarui  (ID buku " + this.id_buku + ")");
            }
        }
    }

    public void jumlahHabis() throws SQLException {
        String sql = "DELETE FROM buku WHERE id_buku = '" + this.id_buku + "'";
        Statement statement = conn.createStatement();

        if (statement.executeUpdate(sql) > 0) {
            System.out.println("Buku sedang tidak ada dengan ID " + this.id_buku + ")");
        }
    }

    public void isiTabelBukuDikembalikan() throws SQLException {
        conn = DriverManager.getConnection(link, "root", "");
        Statement statement = conn.createStatement();
        String sql = "INSERT INTO mengembalikan (nama_peminjam, judul_buku, id_buku, jumlah, nama_petugas, id_petugas)  VALUES ('"
                + this.nama_peminjam + "', '" + this.judul + "','" + this.id_buku + "','"
                + this.jumlah + "', '" + this.nama_petugas + "','" + this.id_petugas + "'";
        statement.execute(sql);
    }

    // override method search dari class Buku
    @Override
    public void search() throws SQLException {
        id_buku();
        conn = DriverManager.getConnection(link, "root", "");
        Statement statement = conn.createStatement();
        String sql = "SELECT * FROM buku WHERE id_buku LIKE'" + this.id_buku + "'";
        ResultSet result = statement.executeQuery(sql);

        if (result.next()) {// jika bernilai true
            bukuTersedia = true;
            this.judul = result.getString("judul");
            this.jumlah = result.getInt("jumlah");
            jumlah(result.getInt("jumlah"));
            System.out.println("Buku ada di rak sebanyak " + this.jumlah + " eksemplar");
        }
    }

    // override method delete dari class Buku
    @Override
    public void delete() throws SQLException {
        try {
            view();// pertama menampilkan data buku yg ada
            System.out.println("Isi data berikut");
            nama_petugas();
            id_petugas();
            search();// akan dijalankan method search

            // percabangna if
            if (bukuTersedia) {// jika jumlah buku tersedia
                jumlahDikembalikan();
                cekJumlah();

                if (bukuCukup) {// apakah bukunya cukup
                    bukuBerkurang();
                    isiTabelBukuDikembalikan();
                } else if (bukuPas) {// apakah buku pas, misalnya buku tersedia 2 dan kita ingin meminjam 2
                    jumlahHabis();
                    isiTabelBukuDikembalikan();
                }
            } else {// jika buku tidak tersedia
                System.out.println("Buku tidak tersedia di rak");
            }
        } catch (SQLException e) {
            System.out.println("Gagal input data");

        } catch (InputMismatchException e) {
            System.out.println("Mohon masukkan data yang benar");
        }
    }
}