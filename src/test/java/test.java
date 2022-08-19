import org.springframework.util.AntPathMatcher;

public class test {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public static void main(String[] args) {
        String[] noCheck = {"/employee/login", "/employee/logout", "/backend/**", "front/**"};
        System.out.println(check("/backend/index.html", noCheck));
        return;
    }

    public static boolean check(String path, String[] urls) {
        for (String url : urls) {
            //match(需要进行校验的路径，存放需要校验的路径的数组)
            if (PATH_MATCHER.match(url, path)) {
                return true;
            }
        }
        return false;
    }
}
