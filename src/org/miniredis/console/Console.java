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
	private static Scanner sc;

	public static void main(String[] args) {
		MiniRedis redis = new MiniRedis();

		sc = new Scanner(System.in);
		String line = null;
		String empty = "";

		int quantity;
		String result;

		while (true) {
			System.out.print(">");
			line = sc.nextLine();
			if (line != null && !line.equals(empty)) {
				String[] commands = line.split("\\s+");
				if (commands.length > 0) {
					switch (commands[0].toUpperCase(Locale.ROOT)) {
					case "SET":
						try {
							if (commands.length == 3)
								result = redis.setCommand(commands[1], commands[2]);
							else if (commands.length == 5 && commands[3].toUpperCase(Locale.ROOT).equals("EX"))
								result = redis.setCommand(commands[1], commands[2], commands[4]);
							else {
								System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "set"));
								break;
							}
							System.out.println(result);
						} catch (NotIntegerOrOutOfRangeException e) {
							System.out.println(String.format(COMMAND_ERROR, e.getMessage()));
						}
						break;

					case "GET":
						if (commands.length > 2)
							System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "get"));
						else
							try {
								System.out.println(redis.getCommand(commands[1]));
							} catch (WrongTypeException e1) {
								System.out.println(String.format(COMMAND_ERROR, e1.getMessage()));
							}
						break;
						
					case "DEL":
						String[] arguments = Arrays.copyOfRange(commands, 1, commands.length);
						quantity = redis.delCommand(arguments);
						System.out.println(String.format(COMMAND_INTEGER, quantity));
						break;

					case "DBSIZE":
						if (commands.length > 1)
							System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "dbsize"));
						quantity = redis.DBSizeCommand();
						System.out.println(String.format(COMMAND_INTEGER, quantity));
						break;

					case "INCR":
						if (commands.length > 2)
							System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "incr"));
						else {
							try {
								quantity = redis.incCommand(commands[1]);
								System.out.println(String.format(COMMAND_INTEGER, quantity));
							} catch (WrongTypeException | NotIntegerOrOutOfRangeException e) {
								System.out.println(String.format(COMMAND_ERROR, e.getMessage()));
							}
						}
						break;
						
					case "ZADD":
						if(commands.length > 4)
							System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "zadd"));
						try {
							quantity = redis.zaddCommand(commands[1], commands[2], commands[3]);
							System.out.println(String.format(COMMAND_INTEGER, quantity));
						} catch (WrongTypeException | NotValidFloatException e) {
							System.out.println(String.format(COMMAND_ERROR, e.getMessage()));
						}
						break;
						
					case "ZCARD":
						if(commands.length > 2)
							System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "zcard"));
						try {
							quantity = redis.zcardCommand(commands[1]);
							System.out.println(String.format(COMMAND_INTEGER, quantity));
						} catch (WrongTypeException e) {
							System.out.println(String.format(COMMAND_ERROR, e.getMessage()));
						}
						break;
						
					case "ZRANK":
						if(commands.length > 3)
							System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "zrank"));
						Object r;
						try {
							r = redis.zrankCommand(commands[1], commands[2]);
							if(r == null)
								System.out.println("(nil)");
							else
								System.out.println(String.format(COMMAND_INTEGER, r));
						} catch (WrongTypeException e) {
							System.out.println(String.format(COMMAND_ERROR, e.getMessage()));
						}
						
					case "ZRANGE":
						if(commands.length > 4)
							System.out.println(String.format(COMMAND_WRONG_ARGUMENTS, "zrange"));
						try {
							List<String> range = redis.zrangeCommand(commands[1], commands[2], commands[3]);
							if(range == null || range.size() == 0)
								System.out.println(String.format(COMMAND_EMPTY_LIST));
							else
							{
								for (int i = 0; i < range.size(); i++) {
									
									System.out.println(String.format("%d) %s", i, range.get(i)));
								}
							}
						} catch (WrongTypeException | NotIntegerOrOutOfRangeException e) {
							System.out.println(String.format(COMMAND_ERROR, e.getMessage()));
						}
						break;
						
					default:
						break;
					}
				}
			}
		}
	}

}
