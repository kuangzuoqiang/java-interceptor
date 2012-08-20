/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.pcan.java.interceptor.entity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Pierantonio
 */
public class LocalVariableTable extends AbstractAttribute {

    private LocalVariableTableEntry[] table;

    public LocalVariableTableEntry[] getTable() {
        return table;
    }

    public void setTable(LocalVariableTableEntry[] table) {
        this.table = table;
    }

    public static class LocalVariableTableEntry {

        private int startPc;
        private int length;
        private int nameIndex;
        private int descriptorIndex;
        private int index;

        public int getDescriptorIndex() {
            return descriptorIndex;
        }

        public void setDescriptorIndex(int descriptorIndex) {
            this.descriptorIndex = descriptorIndex;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getNameIndex() {
            return nameIndex;
        }

        public void setNameIndex(int nameIndex) {
            this.nameIndex = nameIndex;
        }

        public int getStartPc() {
            return startPc;
        }

        public void setStartPc(int startPc) {
            this.startPc = startPc;
        }
    }

    public void refresh(int injectedCodeSize) throws IOException {
        int attrLength = table.length*10; //(5 x short)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2 + attrLength);
        DataOutputStream os = new DataOutputStream(byteArrayOutputStream);
        os.writeShort(table.length);
        for(LocalVariableTableEntry entry : table) {
            int startPc = (entry.getStartPc() == 0 ? 0 : entry.getStartPc() + injectedCodeSize);
            os.writeShort(startPc);
            int length = (entry.getStartPc() == 0 ? entry.getLength() + injectedCodeSize : entry.getLength());
            os.writeShort(length);
            os.writeShort(entry.getNameIndex());
            os.writeShort(entry.getDescriptorIndex());
            os.writeShort(entry.getIndex());
        }
        this.setInfo(byteArrayOutputStream.toByteArray());
    }
}
