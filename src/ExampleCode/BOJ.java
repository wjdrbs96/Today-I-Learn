package ExampleCode;

import java.util.*;

public class BOJ {

    static ArrayList<Integer>[] arrList;
    static boolean [] boold;
    static boolean [] boolb;

    private static void dfs(int now){

        if (boold[now]) {
            return;
        }

        boold[now] = true;
        System.out.print(now + " ");

        for (int i : arrList[now]) {
            if (!boold[i]) {
                dfs(i);
            }
        }
    }

    private static void bfs(int now){
        Queue<Integer> q = new LinkedList<>();
        q.add(now);
        boolb[now] = true;
        while (!q.isEmpty()){
            int temp = q.poll();
            System.out.print(temp + " ");
            for (int i : arrList[temp]) {
                if (!boolb[i]) {
                    boolb[i] = true;
                    q.add(i);
                }
            }
        }
    }


    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        int edge = scan.nextInt();
        int line = scan.nextInt();
        int initEdge = scan.nextInt();

        boolb = new boolean[edge + 1];
        boold = new boolean[edge + 1];
        arrList = new ArrayList[edge + 1];

        for(int i = 1; i <= edge; i++){
            arrList[i] = new ArrayList<>();
        }

        for (int i = 1; i <= line; i++) {
            int a = scan.nextInt();
            int b = scan.nextInt();
            arrList[a].add(b);
            arrList[b].add(a);
        }

        for (int i = 1; i <= line; i++){
            Collections.sort(arrList[i]);
        }

        dfs(initEdge);
        System.out.println();
        bfs(initEdge);
        System.out.println();
        scan.close();
    }
}
