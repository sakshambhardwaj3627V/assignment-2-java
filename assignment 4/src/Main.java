import java.io.*;
import java.util.*;

interface Show{void show();}

abstract class Item implements Show{
    int id; String title;
    Item(int id,String title){this.id=id;this.title=title;}
}

class Book extends Item{
    String auth,cat; boolean issued;
    Book(int id,String t,String a,String c){super(id,t);auth=a;cat=c;}
    void issue(){issued=true;}
    void ret(){issued=false;}
    public void show(){System.out.println(id+"|"+title+"|"+auth+"|"+cat+"|"+issued);}
}

class Member implements Show{
    int mid; String name,email; List<Integer> list=new ArrayList<>();
    Member(int id,String n,String e){mid=id;name=n;email=e;}
    void add(int id){list.add(id);}
    void rem(int id){list.remove(Integer.valueOf(id));}
    public void show(){System.out.println(mid+"|"+name+"|"+email+"|"+list);}
}

class BookErr extends Exception{
    BookErr(String m){super(m);}
}

class Lib{
    Map<Integer,Book> bmap=new HashMap<>();
    Map<Integer,Member> mmap=new HashMap<>();
    int bc=100,mc=200;
    Lib(){load();auto();}
    void addBook(String t,String a,String c){
        Book b=new Book(++bc,t,a,c);
        bmap.put(b.id,b);
        System.out.println("Book ID:"+b.id);
    }
    void addMem(String n,String e){
        Member m=new Member(++mc,n,e);
        mmap.put(m.mid,m);
        System.out.println("Member ID:"+m.mid);
    }
    void issue(int bid,int mid)throws BookErr{
        if(!bmap.containsKey(bid)||!mmap.containsKey(mid))return;
        Book b=bmap.get(bid);
        if(b.issued)throw new BookErr("Issued");
        b.issue();
        mmap.get(mid).add(bid);
        System.out.println("Done");
    }
    void ret(int bid,int mid){
        if(!bmap.containsKey(bid)||!mmap.containsKey(mid))return;
        bmap.get(bid).ret();
        mmap.get(mid).rem(bid);
        System.out.println("Returned");
    }
    void search(String k){
        bmap.values().stream().filter(b->b.title.contains(k)||b.auth.contains(k)||b.cat.contains(k)).forEach(Book::show);
    }
    void sort(){
        bmap.values().stream().sorted(Comparator.comparing(b->b.title)).forEach(Book::show);
    }
    void save(){
        try(BufferedWriter w=new BufferedWriter(new FileWriter("books.txt"))){
            for(Book b:bmap.values())w.write(b.id+","+b.title+","+b.auth+","+b.cat+","+b.issued+"\n");
        }catch(Exception e){}
        try(BufferedWriter w=new BufferedWriter(new FileWriter("members.txt"))){
            for(Member m:mmap.values())w.write(m.mid+","+m.name+","+m.email+","+m.list+"\n");
        }catch(Exception e){}
    }
    void load(){
        try(BufferedReader r=new BufferedReader(new FileReader("books.txt"))){
            String s;while((s=r.readLine())!=null){
                String p[]=s.split(",");
                Book b=new Book(Integer.parseInt(p[0]),p[1],p[2],p[3]);
                b.issued=Boolean.parseBoolean(p[4]);
                bmap.put(b.id,b);bc=Math.max(bc,b.id);
            }
        }catch(Exception e){}
        try(BufferedReader r=new BufferedReader(new FileReader("members.txt"))){
            String s;while((s=r.readLine())!=null){
                String p[]=s.split(",");
                Member m=new Member(Integer.parseInt(p[0]),p[1],p[2]);
                mmap.put(m.mid,m);mc=Math.max(mc,m.mid);
            }
        }catch(Exception e){}
    }
    void auto(){
        Thread t=new Thread(()->{try{while(true){save();Thread.sleep(3000);}}catch(Exception e){}});
        t.setDaemon(true);t.start();
    }
}

class LibrarySystem{
    public static void main(String[]a){
        Lib l=new Lib();
        Scanner s=new Scanner(System.in);
        while(true){
            System.out.println("1 AddBook 2 AddMem 3 Issue 4 Return 5 Search 6 Sort 7 Exit");
            try{
                int c=s.nextInt();
                switch(c){
                    case 1->{
                        s.nextLine();
                        System.out.print("Title: ");String t=s.nextLine();
                        System.out.print("Auth: ");String au=s.nextLine();
                        System.out.print("Cat: ");String c1=s.nextLine();
                        l.addBook(t,au,c1);
                    }
                    case 2->{
                        s.nextLine();
                        System.out.print("Name: ");String n=s.nextLine();
                        System.out.print("Email: ");String e=s.nextLine();
                        l.addMem(n,e);
                    }
                    case 3->{
                        System.out.print("Bid: ");int bid=s.nextInt();
                        System.out.print("Mid: ");int mid=s.nextInt();
                        l.issue(bid,mid);
                    }
                    case 4->{
                        System.out.print("Bid: ");int bid=s.nextInt();
                        System.out.print("Mid: ");int mid=s.nextInt();
                        l.ret(bid,mid);
                    }
                    case 5->{
                        s.nextLine();
                        System.out.print("Key: ");l.search(s.nextLine());
                    }
                    case 6->l.sort();
                    case 7->{l.save();return;}
                }
            }catch(BookErr e){System.out.println(e.getMessage());}
            catch(Exception e){System.out.println("Err");s.nextLine();}
        }
    }
}