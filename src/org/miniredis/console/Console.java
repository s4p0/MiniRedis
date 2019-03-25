package org.miniredis.console;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.miniredis.MiniRedis;
import org.miniredis.exceptions.NotIntegerOrOutOfRangeException;
import org.miniredis.exceptions.NotValidFloatException;
import org.miniredis.exceptions.WrongTypeException;

public class Console {

	public static String COMMAND_EMPTY_LIST = "(empty list or set)";
	public static String COMMAND_WRONG_ARGUMENTS = "(error) ERR wrong number of arguments for '%s' command";
	public static String COMMAND_INTEGER = "(integer) %d";
	public static String COMMAND_ERROR = "(error) %s";
	public static String EXIT = "exit";
	private static Scanner sc;

	public static void setCommand(String[] commands, MiniRedis redis) {
		String result = null;
		try {
			if (commands.length == 3)
				result = redis.setCommand(commands[1], commands[2]);
			else if (commands.length == 5 && commands[3].toUpperCase(Locale.ROOT).equals("EX"))
				result = redis.setCommand(commands[1], commands[2], commands[4]);
			else {
				System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "set"));
				return;
			}
		} catch (NotIntegerOrOutOfRangeException e) {
			System.out.println(String.format(COMMAND_ERROR, e.getMessage()));
		}
		System.out.println(result);
	}

	public static void getCommand(String[] commands, MiniRedis redis) {
		if (commands.length > 2) {
			System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "get"));
			return;
		}

		try {
			System.out.println(redis.getCommand(commands[1]));
		} catch (WrongTypeException e1) {
			System.out.println(String.format(COMMAND_ERROR, e1.getMessage()));
		}
	}

	public static void delCommand(String[] commands, MiniRedis redis) {
		String[] arguments = Arrays.copyOfRange(commands, 1, commands.length);
		int quantity = redis.delCommand(arguments);
		System.out.println(String.format(COMMAND_INTEGER, quantity));
	}

	private static void dbsizeCommand(String[] commands, MiniRedis redis) {
		if (commands.length > 1) {
			System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "dbsize"));
			return;
		}

		int quantity = redis.DBSizeCommand();
		System.out.println(String.format(COMMAND_INTEGER, quantity));
	}

	private static void incrCommand(String[] commands, MiniRedis redis) {
		if (commands.length > 2) {
			System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "incr"));
			return;
		}

		try {
			int quantity = redis.incCommand(commands[1]);
			System.out.println(String.format(COMMAND_INTEGER, quantity));
		} catch (WrongTypeException | NotIntegerOrOutOfRangeException e) {
			System.out.println(String.format(COMMAND_ERROR, e.getMessage()));
		}

	}

	private static void zaddCommand(String[] commands, MiniRedis redis) {
		if (commands.length > 4) {
			System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "zadd"));
			return;
		}

		try {
			int quantity = redis.zaddCommand(commands[1], commands[2], commands[3]);
			System.out.println(String.format(COMMAND_INTEGER, quantity));
		} catch (WrongTypeException | NotValidFloatException e) {
			System.out.println(String.format(COMMAND_ERROR, e.getMessage()));
		}
	}

	private static void zcardCommand(String[] commands, MiniRedis redis) {
		if (commands.length > 2) {
			System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "zcard"));
			return;
		}

		try {
			int quantity = redis.zcardCommand(commands[1]);
			System.out.println(String.format(COMMAND_INTEGER, quantity));
		} catch (WrongTypeException e) {
			System.out.println(String.format(COMMAND_ERROR, e.getMessage()));
		}
	}

	private static void zrankCommand(String[] commands, MiniRedis redis) {
		if (commands.length > 3) {
			System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "zrank"));
			return;
		}

		Object r;

		try {
			r = redis.zrankCommand(commands[1], commands[2]);
			if (r == null)
				System.out.println("(nil)");
			else
				System.out.println(String.format(COMMAND_INTEGER, r));
		} catch (WrongTypeException e) {
			System.out.println(String.format(COMMAND_ERROR, e.getMessage()));
		}
	}

	private static void zrangeCommand(String[] commands, MiniRedis redis)
	{
		if (commands.length > 4) {
			System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "zrange"));
			return;
		}
		
		try {
			List<String> range = redis.zrangeCommand(commands[1], commands[2], commands[3]);
			if (range == null || range.size() == 0)
				System.out.println(String.format(COMMAND_EMPTY_LIST));
			else {
				for (int i = 0; i < range.size(); i++) {
					System.out.println(String.format("%d) %s", i, range.get(i)));
				}
			}
		} catch (WrongTypeException | NotIntegerOrOutOfRangeException e) {
			System.out.println(String.format(COMMAND_ERROR, e.getMessage()));
		}
	}
	
	public static void main(String[] args) {
		MiniRedis redis = new MiniRedis();

		sc = new Scanner(System.in);
		String line = null;
		String empty = "";

		while (true) {
			System.out.print(">");
			line = sc.nextLine();

			if (line.equalsIgnoreCase(EXIT))
				break;

			if (line != null && !line.equals(empty)) {
				String[] commands = line.split("\\s+");
				if (commands.length > 0) {
					switch (commands[0].toUpperCase(Locale.ROOT)) {
					case "SET":
						setCommand(commands, redis);
						break;

					case "GET":
						getCommand(commands, redis);
						break;

					case "DEL":
						delCommand(commands, redis);
						break;

					case "DBSIZE":
						dbsizeCommand(commands, redis);
						break;

					case "INCR":
						incrCommand(commands, redis);
						break;

					case "ZADD":
						zaddCommand(commands, redis);
						break;

					case "ZCARD":
						zcardCommand(commands, redis);
						break;

					case "ZRANK":
						zrankCommand(commands, redis);
						break;

					case "ZRANGE":
						zrangeCommand(commands, redis);
						break;

					default:
						break;
					}
				}
			}
		}
	}

}
