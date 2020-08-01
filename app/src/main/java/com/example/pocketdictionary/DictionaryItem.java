package com.example.pocketdictionary;

public class DictionaryItem {
        private String name;
        private int id;

        public DictionaryItem(int id, String name) {
            this.name = name;
            this.id = id;
        }

        public String getItemName() {

            return this.name;
        }

        public int getItemId() {

            return this.id;
        }

}
