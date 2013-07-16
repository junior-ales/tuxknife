package br.com.tuxknife.helper;

import java.io.Writer;

import br.com.tuxknife.CommandResponse;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class Should {
    private static final String DEFAULT_NEW_LINE = "";
    private static final char[] DEFAULT_LINE_INDENTER = {};

    public static String getJsonOf(CommandResponse commandResponse, boolean rootJsonElement) {
        return getXStreamJSON(rootJsonElement).toXML(commandResponse);
    }

    private static XStream getXStreamJSON(final boolean rootJsonElement) {
        return new XStream(new JsonHierarchicalStreamDriver(){
            public HierarchicalStreamWriter createWriter(Writer writer) {
                if (rootJsonElement) {
                    return new JsonWriter(writer, DEFAULT_LINE_INDENTER, DEFAULT_NEW_LINE, JsonWriter.DROP_ROOT_MODE);
                }
                return new JsonWriter(writer, DEFAULT_LINE_INDENTER, DEFAULT_NEW_LINE);
            }
        });
    }
}
