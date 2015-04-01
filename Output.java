public class Output {
	public static void Green(String input){
		System.out.print("\033[32m" + input + "\033[0m");
	}
	public static void Greenln(String input){
		System.out.println("\033[32m" + input + "\033[0m");
	}
	public static void Blue(String input){
		System.out.print("\033[34m" + input + "\033[0m");
	}
	public static void Blueln(String input){
		System.out.println("\033[34m" + input + "\033[0m");
	}
	public static void Cyan(String input){
		System.out.print("\033[36m" + input + "\033[0m");
	}
	public static void Cyanln(String input){
		System.out.println("\033[36m" + input + "\033[0m");
	}
	public static void Red(String input){
		System.out.print("\033[31m" + input + "\033[0m");
	}
	public static void Redln(String input){
		System.out.println("\033[31m" + input + "\033[0m");
	}
	public static void Yellow(String input){
		System.out.print("\033[33m" + input + "\033[0m");
	}
	public static void Yellowln(String input){
		System.out.println("\033[33m" + input + "\033[0m");
	}
}