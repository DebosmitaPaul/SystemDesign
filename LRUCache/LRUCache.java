import java.util.*;

public class LRUCache {
    int capacity;
    Map<Integer,DLLNode> cacheMap;
    DLLNode head;
    DLLNode tail;
    DLinkedList linkedList;
    LRUCache(int capacity){
        this.capacity= capacity;
        cacheMap = new HashMap<>();
        this.head = new DLLNode(0,0);
        this.tail = new DLLNode(0,0);
        this.linkedList = new DLinkedList(head,tail);
        head.next = tail;
        tail.prev = head;
    }
    void put(int key, int value){
        if(cacheMap.containsKey(key)) {
            DLLNode node = cacheMap.get(key);
            linkedList.remove(node);
            node.value = value;
            linkedList.addFirst(node);
        }else {
            if (cacheMap.size() < capacity) {
                DLLNode node = new DLLNode(key, value);
                linkedList.addFirst(node);
                cacheMap.put(key, node);
            } else {
                DLLNode lastNode = linkedList.removeFromLast();
                DLLNode node = new DLLNode(key, value);
                linkedList.addFirst(node);
                cacheMap.remove(lastNode.key);
                cacheMap.put(key, node);
            }
        }
    }
    int get(int key){
        if(!cacheMap.containsKey(key)){
            return -1;
        }else{
           DLLNode node = cacheMap.get(key);
           linkedList.remove(node);
           linkedList.addFirst(node);
           return node.value;
        }
    }
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(4);
        cache.put(2,6);
        cache.put(4,7);
        cache.put(8,11);
        cache.put(7,10);
        System.out.println(cache.get(2));
        System.out.println(cache.get(8));
        cache.put(5,6);
//        cache.put(6,9);
        System.out.println(cache.get(7));
        cache.put(5,7);
        System.out.println(cache.get(5));
    }
}
class DLLNode{
    int key;
    int value;
    DLLNode prev;
    DLLNode next;
    DLLNode(int key, int value){
        this.key = key;
        this.value=value;
        prev=null;
        next=null;
    }
}
class DLinkedList{
    DLLNode head;
    DLLNode tail;
    DLinkedList(DLLNode head, DLLNode tail){
        this.head = head;
        this.tail = tail;
    }
    void addFirst(DLLNode node){
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
        node.prev=head;
    }
    void remove(DLLNode currNode){
        DLLNode prevNode = currNode.prev;
        DLLNode nextNode = currNode.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }
    DLLNode removeFromLast(){
        DLLNode lastNode = tail.prev;
        tail.prev = lastNode.prev ;
        lastNode.prev.next = tail;
        return lastNode;
    }
}
