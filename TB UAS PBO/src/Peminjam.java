public class Peminjam extends Orang {
    // deklarasi
    String id_peminjam;

    // contructor
    public Peminjam() {

    }

    // constructor
    public Peminjam(String nama_peminjam, String id_peminjam) {
        this.nama_peminjam = nama_peminjam;
        this.id_peminjam = id_peminjam;
        System.out.println(this.nama_peminjam + " adalah peminjam baru");
    }

    public void methodKosong() {

    }
}
