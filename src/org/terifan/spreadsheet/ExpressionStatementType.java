package org.terifan.spreadsheet;

import java.util.function.Function;
import java.util.regex.Pattern;
import static org.terifan.spreadsheet.Convert.toDouble;


public enum ExpressionStatementType
{
	PLUS("\\+", e->e),
	MINUS("\\-", e->e),
	MUL("\\*", e->e),
	DIV("\\/", e->e),
	REF("\\!", e->e),
	LPARAN("\\(", e->e),
	RPARAN("\\)", e->e),
	NAME("\\[.*\\]", e->e),
	TERM("[\\<\\>\\=\\~\\^\\%\\&]", e->e),
	SWITCH("\\?", e->e),
	SEPARATOR("\\,", e->e),
	NUMBER("[\\+\\-]?[0-9.,]+", e -> toDouble(e)),
	LITERAL("[\\'\\\"].*", e->e),
	RANGE("[a-zA-Z$]+[0-9]+\\:[a-zA-Z$]+[0-9]+", e->new Range(e)),
	TUPLE("[a-zA-Z$]+[0-9]+", e->new Tuple(e)),
	FUNCTION("[a-zA-Z]+\\(", e->e.substring(0,e.length() - 1));

	private Pattern mPattern;
	private Function<String,Object> mConverter;

	ExpressionStatementType(String aPattern, Function<String,Object> aConverter)
	{
		mPattern = Pattern.compile(aPattern);
		mConverter = aConverter;
	}


	public Pattern getPattern()
	{
		return mPattern;
	}


	public Function<String, Object> getConverter()
	{
		return mConverter;
	}
}
