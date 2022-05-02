package stub;

/**
 *  计算器服务
 *
 * @author luolinyuan
 * @date 2022/4/11
 **/
public interface Calculator {
	/**
	 * 整型加法
	 * @param a
	 * @param b
	 * @return int
	 **/
	int add(int a, int b);

	/**
	 * 整形减法
	 * @param a
	 * @param b
	 * @return int
	 **/
	int minus(int a, int b);

	double add(double a, double b);
}
