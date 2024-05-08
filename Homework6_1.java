import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Homework6_1 {

    public static class Node {
        public int val;
        public List<Node> children;

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    public static class MaxDepthTask extends RecursiveTask<Integer> {
        private final Node node;

        public MaxDepthTask(Node node) {
            this.node = node;
        }

        @Override
        protected Integer compute() {
            if (node == null) {
                return 0; // Базовый случай для рекурсивных подзадач, когда узел равен null.
            }
            if (node.children == null || node.children.isEmpty()) {
                return 1;
            }

            int max = 0;
            for (Node child : node.children) {
                MaxDepthTask task = new MaxDepthTask(child);
                task.fork(); // Асинхронное выполнение этой задачи
                int depth = task.join(); // Ожидание результата
                max = Math.max(max, depth);
            }

            return max + 1;
        }
    }

    public int maxDepth(Node root) {
        if (root == null) {
            throw new IllegalArgumentException("Корень не может быть null");
        }
        ForkJoinPool pool = ForkJoinPool.commonPool();
        return pool.invoke(new MaxDepthTask(root));
    }
}
