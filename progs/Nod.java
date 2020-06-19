//dgfdfd
//dgf
 void main() {
        int a = System.in.read();
        int b = System.in.read();
        while (b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }

        System.out.println(a);
    }
