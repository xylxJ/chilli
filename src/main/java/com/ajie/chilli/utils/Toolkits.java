package com.ajie.chilli.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

/**
 * 工具箱
 * 
 * @author niezhenjie
 */
final public class Toolkits {

	/** 数字字母表 */
	public final static char[] digits = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };

	/** 十六进制对照表 */
	public final static char[] hexTable = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static final Random _Random = new Random();

	private Toolkits() {
	}

	/**
	 * 生成由0-9a-zA-z组成的长度为16的字串
	 * 
	 * @return
	 */
	static public String uniqueKey() {
		return uniqueKey(16);
	}

	/**
	 * 生成长度为len的随机数，返回的是字符串，前面为0也显示
	 * 
	 * @param len
	 * @return
	 */
	static public String randomNum(int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int idx = getRandomRange(0, 9);
			sb.append(digits[idx]);
		}
		return sb.toString();
	}

	/**
	 * 生成由0-9a-z组成的唯一名字，经测试，此方法的唯一性最高，生成16位数，在5000个并发下测试10次没有出现过冲突
	 * 但是这个在性能上消耗大于genRandomStr，在并发很大时，优先选择此方法
	 * 
	 * @param len
	 * @return
	 */
	static public String uniqueKeyLowerCase(int len) {
		if (0 == len)
			return "";
		int timestamptlen = 13;// 时间戳长度
		StringBuilder sb = new StringBuilder();
		if (len < timestamptlen) { // 长度少于13，直接使用13个随机数做对照
			for (int i = 0; i < len; i++) {
				int idx = getRandomRange(0, 36);
				sb.append(digits[idx]);
			}
			return sb.toString();
		}
		len -= timestamptlen;
		for (int i = 0; i < len; i++) {
			int idx = getRandomRange(0, 36);
			sb.append(digits[idx]);
		}
		Date now = new Date();
		sb.append(now.getTime());
		return sb.toString();

	}

	/**
	 * 生成由0-9a-zA-Z组成的唯一名字，经测试，此方法的唯一性最高，生成16位数，在5000个并发下测试10次没有出现过冲突
	 * 但是这个在性能上消耗大于genRandomStr，在并发很大时，优先选择此方法
	 * 
	 * @param len
	 * @return
	 */
	static public String uniqueKey(int len) {
		if (0 == len)
			return "";
		int timestamptlen = 13;// 时间戳长度
		StringBuilder sb = new StringBuilder();
		if (len < timestamptlen) { // 长度少于13，直接使用13个随机数做对照
			for (int i = 0; i < len; i++) {
				int idx = getRandomRange(0, 61);
				sb.append(digits[idx]);
			}
			return sb.toString();
		}
		len -= timestamptlen;
		for (int i = 0; i < len; i++) {
			int idx = getRandomRange(0, 61);
			sb.append(digits[idx]);
		}
		Date now = new Date();
		sb.append(now.getTime());
		return sb.toString();
	}

	/**
	 * 线程异常堆栈
	 * 
	 * @param e
	 * @return
	 */
	public static String printTrace(Throwable e) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} finally {
			try {
				if (null != sw) {
					sw.close();
				}
				if (null != pw) {
					pw.close();
				}
			} catch (Exception ex) {
				// Ignore
			}
		}
	}

	/**
	 * 16位随机数 <br>
	 * 当前时间戳+从6为随机数里截取的三位数，经测试，同时开100个线程生成的id没有出现过重复<br>
	 * 
	 * @return
	 * 
	 * @Deprecated 使用genRandomStr或uniqueKey
	 */
	@Deprecated
	public static String gen16UniqueId() {
		long currentTimeMillis = System.currentTimeMillis(); // 当前时间戳
		Random random = new Random();
		int random1 = random.nextInt(999); // 3位随机数
		int random2 = random.nextInt(999); // 3位随机数
		int radom = ((random1 * random2));
		StringBuilder sb = new StringBuilder();
		sb.append(currentTimeMillis);
		sb.append(radom);
		String str = sb.toString();
		// 不足16位用0补足
		int lack = 16 - str.length();
		if (lack > 0) {
			while (lack-- > 0) {
				str += "0";
			}
		} else if (lack < 0) {
			str = str.substring(0, 16);
		}
		return str;
	}

	/**
	 * 随机生成指定长度的字串，长度越小，冲突的几率越大，经测试5000个并发生成16位冲突在10个以内，在并发不是很大时，可以使用，并发大时，
	 * 请使用uniqueKey，此方法性能高于uniqueKey
	 * 
	 * @param len
	 * @return
	 */
	public static String genRandomStr(int len) {
		StringBuilder sb = new StringBuilder();
		String rbt = genRandomByTimestamp(sb);
		if (rbt.length() > len)
			return rbt.substring(0, len);
		if (rbt.length() == len)
			return rbt;
		while (rbt.length() < len) {
			rbt = genRandomByTimestamp(sb);
		}
		return rbt.substring(0, len);
	}

	/**
	 * 当前时间戳每一个数加上一个随机数再根据对照表获得一个13位的字串
	 * 
	 * 时间戳是13位，到2286年失效
	 * 
	 * @return
	 */
	public static String genRandomByTimestamp(StringBuilder sb) {
		String now = String.valueOf(new Date().getTime());
		int rad = getRandomRange(0, 52);// 对照表最大下标为61，now单个数最大值是9
		if (null == sb)
			sb = new StringBuilder();
		for (int i = 0; i < 13; i++) {
			int dec = getIndexByChar(now.charAt(i));
			sb.append(digits[dec + rad]);
		}
		return sb.toString();
	}

	/**
	 * 字符c在对照表digits里对象的下标
	 * 
	 * @param c
	 * @return
	 */
	private static int getIndexByChar(char c) {
		for (int i = 0; i < digits.length; i++) {
			char ch = digits[i];
			if (ch == c) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 十进制转十六进制,不显示前缀
	 * 
	 * @param deci
	 * @return
	 * @throws NumberFormatException
	 */
	public static String deci0x2Hex(int deci) throws NumberFormatException {
		return deci2Hex(deci, null);
	}

	/**
	 * 十进制转十六进制
	 * 
	 * @param deci
	 * @param prefix指定结果前缀
	 *            ，结果包括：0x、x、null,不区分大小写，null表示不显示前缀
	 * @return
	 * @throws NumberFormatException
	 */
	public static String deci2Hex(int deci, String prefix)
			throws NumberFormatException {
		if ("x".equalsIgnoreCase(prefix) && "0x".equalsIgnoreCase(prefix)
				&& null != prefix)
			throw new NumberFormatException("格式错误，结果前缀需是0x或x或空,当前前缀：" + prefix);
		String ret = Integer.toHexString(deci);
		return prefix + ret;

	}

	/**
	 * 0x开头的十六进制转十进制
	 * 
	 * @param hex
	 * @return
	 * 
	 * @Deprecated请使用hex2deci
	 */
	@Deprecated
	public static int Hex2Deci(String hex) throws NumberFormatException {
		if (null == hex) {
			throw new NumberFormatException("格式错误，参数格式应为0x开头的十六进制: " + hex);
		}
		int len = hex.length();
		if (len <= 2) {
			throw new NumberFormatException("格式错误，参数格式应为0x开头的十六进制: " + hex);
		}
		String str = hex.substring(2, len);
		int ret = 0;
		try {
			ret = Integer.valueOf(str, 16);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("格式错误，参数格式应为0x开头的十六进制: " + hex);
		}
		return ret;
	}

	/**
	 * 十六进制转十进制
	 * 
	 * @param hex
	 *            十六进制 支持0x开头
	 * @return
	 */
	public static int hex2deci(String hex) {
		if (null == hex)
			throw new NumberFormatException("param is null");
		// 去除前面的0x
		if (hex.startsWith("0x"))
			hex = hex.substring(2);
		if (hex.length() == 0)
			throw new NumberFormatException("param is invalid: " + hex);
		int len = hex.length();
		int i = 0;
		int result = 0;
		while (i < len) {
			int dec = getHexChar(hex.charAt(i));
			if (-1 == dec)
				throw new NumberFormatException("param is invalid: " + hex);
			result += dec << ((len - (i++) - 1) * 4);
		}
		return result;
	}

	/**
	 * 十六进制的字符对应的十进制数字
	 * 
	 * @param c
	 * @return
	 */
	private static int getHexChar(char c) {
		for (int i = 0; i < hexTable.length; i++) {
			char hex = hexTable[i];
			if (hex <= 90 && hex >= 65) // 大写转小写
				hex += 32;
			if (hex == c)
				return i;
		}
		return -1;
	}

	/**
	 * 数字型的十六进制转十进制
	 * 
	 * @param hex
	 * @return
	 * @throws NumberFormatException
	 */
	public static int Hex2Deci(int hex) throws NumberFormatException {
		return hex2deci(String.valueOf(hex));
	}

	/***
	 * 字节数组转16进制字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byte2Hex(byte[] bytes) {
		StringBuilder buf = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) { // 使用String的format方法进行转换
			buf.append(String.format("%02x", new Integer(b & 0xff)));
		}
		return buf.toString();

	}

	/**
	 * 生成32位md5码
	 * 
	 * @param password
	 * @return
	 */
	public static String md5Password(String password) {

		try {
			// 得到一个信息摘要器
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			// 把每一个byte 做一个与运算 0xff;
			for (byte b : result) {
				// 与运算
				int number = b & 0xff;
				String str = Integer.toHexString(number);
				if (str.length() == 1) {
					buffer.append("0");
				}
				buffer.append(str);
			}

			// 标准的md5加密后的结果
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			// e.printStackTrace();
			return "";
		}

	}

	/**
	 * 将字符串转换成数字
	 * 
	 * @param str
	 * @return
	 */
	static public int toInt(String str) {
		if (null == str)
			throw new IllegalArgumentException("转换字符为空");
		try {
			Integer ret = Integer.valueOf(str);
			return ret;
		} catch (Exception e) {
			throw new IllegalArgumentException("无法转换为数字【" + str + "】");
		}

	}

	/**
	 * 将字符串转换成数字
	 * 
	 * @param str
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	static public int toInt(String str, int defaultValue) {
		if (null == str)
			return defaultValue;
		try {
			Integer ret = Integer.valueOf(str);
			return ret;
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * 随机生成从 [min - max]随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomRange(int min, int max) {
		int ret = _Random.nextInt(max - min + 1);
		return ret + min;
	}

	/**
	 * 超简单的32位整数hash算法
	 * 
	 * @param str
	 *            源字串
	 * @param sign
	 *            基础hash值
	 * @return 在基础值上hash源字串后的最终值
	 */
	public static int hashInt32(String str, int sign) {
		if (null == str || 0 == str.length())
			return sign;
		int sum;
		int h;
		int i = 0;
		int a;
		int len = str.length();
		sum = ((sign >> 16) & 0xFFFF);
		h = (sign & 0xFFFF);
		while (i < len) {
			a = str.charAt(i++);
			if (a < 256) {
				a = a << 8;
				if (i < len) {
					a |= str.charAt(i);
					a *= (i++);
				} else {
					a *= i;
				}
			} else {
				a *= i;
			}
			sum += sum ^ a;
			h += a;
			h &= 0xFFFF;
			sum &= 0xFFFF;
			// _Logger.trace("sum="+sum+" h="+h+" i="+i);
		}
		if (sum > 0x7FFF) {
			long fix = sum;
			fix <<= 16;
			fix |= h;
			return (int) (fix - 0x100000000L);
		}
		return ((sum << 16) | h);
	}

	/**
	 * 64位整数hash算法
	 * 
	 * @param str
	 * @param sign
	 * @return
	 */
	public static long hashInt64(String str, int sign) {
		if (null == str || 0 == str.length()) {
			return sign;
		}
		long hash = hashInt32(str, sign);
		hash = (hash << 32) >>> 32;
		hash |= (long) hashCode(str) << 32;
		return hash;
	}

	private static int hashCode(String str) {
		/* 从jdk提取的算法，避免不同jdk算法不同，导致结果不一致 */
		int h = 0;
		for (int i = 0; i < str.length(); i++) {
			int ch = str.charAt(i);
			h = 31 * h + ch;
		}
		return h;
	}

	/**
	 * 64位整数HEX字串，不足16个字符前端补0
	 * 
	 * @param val
	 *            整数
	 * @return hex格式串
	 */
	public static String toHex64(long val) {
		if (0 == val) {
			return "0000000000000000";
		}
		return toHexFixed(val, new StringBuilder(16)).toString();
	}

	/**
	 * 32位整数HEX字串，不足8个字符前端补0
	 * 
	 * @param val
	 *            整数
	 * @return hex格式串
	 */
	public static String toHex32(int val) {
		if (0 == val) {
			return "00000000";
		}
		return toHexFixed(val, new StringBuilder(8)).toString();
	}

	/**
	 * 16位整数HEX字串，不足4个字符前端补0
	 * 
	 * @param val
	 * @return
	 */
	public static String toHex16(short val) {
		if (0 == val) {
			return "0000";
		}
		return toHexFixed(val, new StringBuilder(4)).toString();
	}

	/**
	 * 转为 HEX字串
	 * 
	 * @param val
	 *            32位数值
	 * @param sb
	 *            转换HEX后的追加字串缓冲区
	 * @return 追加后的字串缓冲区
	 */
	public static StringBuilder toHex(int val, StringBuilder sb) {
		if (val < 0 || val >= 0x10000000) {
			sb.append(hexTable[(val >> 28) & 0xF]);
			sb.append(hexTable[(val >> 24) & 0xF]);
			sb.append(hexTable[(val >> 20) & 0xF]);
			sb.append(hexTable[(val >> 16) & 0xF]);
			sb.append(hexTable[(val >> 12) & 0xF]);
			sb.append(hexTable[(val >> 8) & 0xF]);
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x01000000) {
			sb.append(hexTable[(val >> 24) & 0xF]);
			sb.append(hexTable[(val >> 20) & 0xF]);
			sb.append(hexTable[(val >> 16) & 0xF]);
			sb.append(hexTable[(val >> 12) & 0xF]);
			sb.append(hexTable[(val >> 8) & 0xF]);
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x00100000) {
			sb.append(hexTable[(val >> 20) & 0xF]);
			sb.append(hexTable[(val >> 16) & 0xF]);
			sb.append(hexTable[(val >> 12) & 0xF]);
			sb.append(hexTable[(val >> 8) & 0xF]);
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x00010000) {
			sb.append(hexTable[(val >> 16) & 0xF]);
			sb.append(hexTable[(val >> 12) & 0xF]);
			sb.append(hexTable[(val >> 8) & 0xF]);
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x00001000) {
			sb.append(hexTable[(val >> 12) & 0xF]);
			sb.append(hexTable[(val >> 8) & 0xF]);
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x00000100) {
			sb.append(hexTable[(val >> 8) & 0xF]);
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x00000010) {
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x00000001) {
			sb.append(hexTable[(val) & 0xF]);
		} else {
			sb.append("0");
			return sb;
		}
		return sb;
	}

	/**
	 * 32位整数HEX字串，不足8个字符前端补0
	 * 
	 * @param val
	 *            32位数字
	 * @param sb
	 *            字串缓冲区，若为null自动创建新的
	 * @return 8字符的HEX编码串
	 */
	public static StringBuilder toHexFixed(int val, StringBuilder sb) {
		if (null == sb) {
			sb = new StringBuilder(8);
		}
		if (val < 0 || val >= 0x10000000) {
			sb.append(hexTable[(val >> 28) & 0xF]);
			sb.append(hexTable[(val >> 24) & 0xF]);
			sb.append(hexTable[(val >> 20) & 0xF]);
			sb.append(hexTable[(val >> 16) & 0xF]);
			sb.append(hexTable[(val >> 12) & 0xF]);
			sb.append(hexTable[(val >> 8) & 0xF]);
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x01000000) {
			sb.append('0');
			sb.append(hexTable[(val >> 24) & 0xF]);
			sb.append(hexTable[(val >> 20) & 0xF]);
			sb.append(hexTable[(val >> 16) & 0xF]);
			sb.append(hexTable[(val >> 12) & 0xF]);
			sb.append(hexTable[(val >> 8) & 0xF]);
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x00100000) {
			sb.append("00");
			sb.append(hexTable[(val >> 20) & 0xF]);
			sb.append(hexTable[(val >> 16) & 0xF]);
			sb.append(hexTable[(val >> 12) & 0xF]);
			sb.append(hexTable[(val >> 8) & 0xF]);
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x00010000) {
			sb.append("000");
			sb.append(hexTable[(val >> 16) & 0xF]);
			sb.append(hexTable[(val >> 12) & 0xF]);
			sb.append(hexTable[(val >> 8) & 0xF]);
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x00001000) {
			sb.append("0000");
			sb.append(hexTable[(val >> 12) & 0xF]);
			sb.append(hexTable[(val >> 8) & 0xF]);
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x00000100) {
			sb.append("00000");
			sb.append(hexTable[(val >> 8) & 0xF]);
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x00000010) {
			sb.append("000000");
			sb.append(hexTable[(val >> 4) & 0xF]);
			sb.append(hexTable[(val) & 0xF]);
		} else if (val >= 0x00000001) {
			sb.append("0000000");
			sb.append(hexTable[(val) & 0xF]);
		} else {
			sb.append("00000000");
			return sb;
		}
		return sb;
	}

	/**
	 * 64位整数HEX字串，不足16个字符前端补0
	 * 
	 * @param val
	 *            64位数值
	 * @param sb
	 *            字串缓冲区，若为null自动创建新的
	 * @return 16字符的HEX编码串
	 */
	public static StringBuilder toHexFixed(long val, StringBuilder sb) {
		if (null == sb) {
			sb = new StringBuilder(16);
		}
		// 高32位
		int i32 = (int) ((val >> 32) & 0xFFFFFFFF);
		toHexFixed(i32, sb);
		// 低32位
		i32 = (int) (val & 0xFFFFFFFF);
		toHexFixed(i32, sb);
		return sb;
	}

	// 测试
	public static void main(String[] args) throws InterruptedException {

		int hex2deci = hex2deci("0x001");
		System.out.println(hex2deci);
		System.out.println("========");

		String uniqueKey = uniqueKey(32);
		System.out.println(uniqueKey);

		System.out.println("==========");

		String s = byte2Hex("我们".getBytes());
		System.out.println(s);

		long l = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		while (l >> 4 > 0) {
			sb.append(String.format("%02x", new Integer((int) (l & 0xff))));
		}

		/*
		 * final HashSet<String> set = new HashSet<String>(); final
		 * ArrayList<String> list = new ArrayList<String>(); for (int i = 0; i <
		 * 5000; i++) { new Thread(new Runnable() {
		 * 
		 * @Override public void run() { synchronized (set) { String id =
		 * Toolkits.uniqueKey(16); // String id = gen16UniqueId(); //
		 * System.out.println(id); set.add(id); list.add(id); } } }).start(); }
		 * try { Thread.sleep(3000); } catch (InterruptedException e) {
		 * e.printStackTrace(); } System.out.println(set.size());
		 * System.out.println(list.size());
		 */
		/*
		 * System.out.println("================================"); for (int i =
		 * 0; i < list.size(); i++) { String str1 = list.get(i); for (int j = i
		 * + 1; j < list.size() - i - 1; j++) { if (str1.equals(list.get(j))) {
		 * System.out.println("i=" + i + ", j=" + j); System.out.println(str1);
		 * } } } System.out.println("================================");
		 */
		/*
		 * for (int i = 0; i < list.size(); i++) { System.out.println(i + "： " +
		 * list.get(i)); }
		 */

		/*
		 * int i = Toolkits.Hex2Deci("0x100"); System.out.println(i);
		 */
	}
}
