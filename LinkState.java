
import java.util.Scanner;

public class LinkState {

    public static void main(String[] args){

        String input = "";
        Scanner scan = new Scanner(System.in);
        String line = " ";

        while(!line.equals("")){
            line = scan.nextLine();
            input = input.concat(line);
            input = input.concat("\n");
        }

        String diff_args[] = input.split("\n");

        String num_links_str = diff_args[0];
        String border_routers3 = diff_args[diff_args.length-1];
        String border_routers2[] = border_routers3.split(" ");
        int[] border_routers = new int[border_routers2.length];

        for(int x = 0;x<border_routers2.length;x++){
            border_routers[x] =Integer.parseInt(border_routers2[x]);
        }

        int num_links = Integer.parseInt(diff_args[0]);

        System.out.println("Number of Links: " + num_links);

        System.out.print("Border Routers: ");

        for(int x =0;x<border_routers.length;x++){
            System.out.print(border_routers[x]+ " ");
        }
        System.out.println("");

        String temp = null;
        String temp_line[];
        int temp_num;
        int adjacency_matrix[][] = new int[num_links][num_links];

        for(int x=1;x<(num_links+1);x++){
            temp = diff_args[x];
            temp_line = temp.split(" ");
            for(int i=0;i<num_links;i++) {
                temp_num = Integer.parseInt(temp_line[i]);
                adjacency_matrix[x-1][i] = temp_num;
            }
        }

        boolean to_run = true;
        for(int x = 0;x<num_links;x++){
            to_run = true;
            for(int i = 0; i<border_routers.length;i++){
                if (x+1 == border_routers[i]){
                    to_run = false;
                    break;
                }
            }
            if (to_run == true){
                Dijkstra_Algorithm(adjacency_matrix, x+1, border_routers);
            }
        }
    }


    public static String Dijkstra_Algorithm(int graph[][], int node, int border_routers[]){

        int num_links = graph.length;

        int noder = node-1;

        int[] shortest_paths = new int[num_links];
        int[] next_hops = new int[num_links];
        boolean visited[] = new boolean[num_links];


        for (int x = 0;x<num_links;x++){
            shortest_paths[x] = -1;
            next_hops[x] = -1;
            visited[x] = false;
        }
        shortest_paths[node-1] = 0;

        int temp_link;
        int temp_path;


        int alternative_hops[] = new int[num_links];
        int alternative_cost[] = new int[num_links];

        for (int x = 0;x<num_links;x++) {
            alternative_cost[x] = -1;
            alternative_hops[x] = -1;
            temp_link = graph[noder][x];
            if (temp_link != -1) {
                shortest_paths[x] = temp_link;
                next_hops[x] = x+1;
            }
        }

        int current_next_hop = 0;
        int j;

        boolean all_visited = false;
        boolean is_visited;

        visited[noder] = true;
        int min;

        int next_node;

        boolean connected = true;

        int alternative_hop2 = -1;
        int alternative_hop = -1;

        while(all_visited == false) {
            min = Integer.MAX_VALUE;
            next_node = 0;
            for (int x = 0; x < num_links; x++) {
                temp_link = shortest_paths[x];
                is_visited = visited[x];

                if (temp_link > 0 && is_visited == false){
                    if (temp_link <= min){
                        next_node = x;
                        current_next_hop = next_hops[x];
                        alternative_hop = alternative_hops[x];
                        min = temp_link;
                    }
                }
            }

            if (min == Integer.MAX_VALUE){
                connected = false;
                break;
            }

            visited[next_node] = true;

            for (int i = 0; i < num_links; i++) {
                temp_path = graph[next_node][i];
                if (temp_path > 0 ){
                    if ((min+temp_path)< shortest_paths[i]){
                        shortest_paths[i] = min+temp_path;
                        next_hops[i] = current_next_hop;
                        if (next_node == alternative_hop){
                            alternative_cost[i] = min+temp_path;
                            alternative_hops[i] = alternative_hop2;
                        }
                    }
                    else if((min+temp_path) == shortest_paths[i]){
                        alternative_cost[i] = min+temp_path;
                        alternative_hops[i] = current_next_hop;
                        alternative_hop = i;
                        alternative_hop2 = current_next_hop;

                    }
                    else if(shortest_paths[i] == -1){
                        shortest_paths[i] = min+temp_path;
                        next_hops[i] = current_next_hop;
                    }
                }
            }

            all_visited = true;
            for (int x = 0; x < num_links; x++){
                if (visited[x] == false){
                    all_visited = false;
                }
            }
        }


        if (connected == false){
            System.out.println();

            System.out.println("Forwarding Table for "+node+ ":");
            System.out.println("To  Cost  Next Hop");

            boolean to_print = true;

            for(int i =0;i<border_routers.length;i++){
                int the_index = border_routers[i]-1;
                int the_cost = shortest_paths[the_index];
                if (alternative_cost[the_index] == the_cost &&  next_hops[the_index] != alternative_hops[the_index]){
                    System.out.println(border_routers[i]+"    "+the_cost+ "     "+next_hops[the_index]+ "," +alternative_hops[the_index]);
                }
                else{
                    System.out.println(border_routers[i]+"    "+the_cost+ "     "+next_hops[the_index]);
                }
            }
        }
        else{
            next_hops[noder] = 0;

            System.out.println();

            System.out.println("Forwarding Table for "+node+ ":");
            System.out.println("To  Cost  Next Hop");


            for(int i =0;i<border_routers.length;i++){
                int the_index = border_routers[i]-1;
                int the_cost = shortest_paths[the_index];
                if (alternative_cost[the_index] == the_cost &&  next_hops[the_index] != alternative_hops[the_index]){
                    System.out.println(border_routers[i]+"    "+the_cost+ "     "+next_hops[the_index]+ "," +alternative_hops[the_index]);
                }
                else{
                    System.out.println(border_routers[i]+"    "+the_cost+ "     "+next_hops[the_index]);
                }
            }

        }


        return null;
    }


    public static void Print_2d_Array(int graph[][]){
        int num_links = graph.length;
        for(int x=0;x<num_links;x++){
            System.out.println("");
            for(int i=0;i<num_links;i++) {
                System.out.print(graph[x][i] + " ");

            }
        }
    }

    public static void Print_Array(int graph[]){
        int num_links = graph.length;
        System.out.println();
        for(int x=0;x<num_links;x++){
            System.out.print(graph[x]+ " ");
        }
        System.out.println();
    }

    public static void Print_Boolean_Array(boolean graph[]){
        int num_links = graph.length;
        System.out.println();
        for(int x=0;x<num_links;x++){
            System.out.print(graph[x]+ " ");
        }
        System.out.println();
    }

}
