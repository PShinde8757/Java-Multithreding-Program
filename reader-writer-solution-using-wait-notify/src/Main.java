
class Message {
    String msg;
    boolean isMsgEmpty;

    public Message(String msg, boolean isMsgEmpty) {
        this.msg = msg;
        this.isMsgEmpty = isMsgEmpty;
    }

    public synchronized String read() {
        while (this.isMsgEmpty){
            try {
                this.wait();
                // wait till a message is written by Writer Thread
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        this.isMsgEmpty = true;
        this.notifyAll();
        return this.msg ;
    }

    public synchronized void write(String msg) {
        while (!this.isMsgEmpty){
            try {
                this.wait();
                //wait till there is already a message
            } catch (InterruptedException e){
                e.printStackTrace();
            }

        }
        this.msg = msg;
        this.isMsgEmpty = false;

        this.notifyAll();
    }
}

class Reader implements Runnable    {

    Message message;

    public Reader(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        for (String msg = this.message.read();
             !"Finished Writing!!".equals(msg) ; msg = this.message.read() ) {
            System.out.println("Message Read by Reader : "+msg);                    //we are going to print for loop 'msg'
//            System.out.println("Message Read by Reader : "+this.message.read());  //not this class 'massage'
        }
    }
}

class Writer implements Runnable {

    Message message;

    public Writer(Message message) {
        this.message = message;
    }

    @Override
    public void run() {

        String[] messages = {"Hello", "How","are","you"};

        for (String msg: messages) {

            this.message.write(msg);
            System.out.println("Message Read by Writer : "+msg);

        }

        this.message.write("Finished Writing!!");

    }
}

public class Main {
    public static void main(String[] args) {
        Message message = new Message("",true);

        Thread readerThread = new Thread(new Reader(message));
        readerThread.setName("ReaderThread");

        Thread writerThread = new Thread(new Writer(message));
        writerThread.setName("WriterThread");
        readerThread.start();
        writerThread.start();
    }
}