package com.tianpl.algorithms;


import lombok.Getter;
import lombok.Setter;

/**
 * Created by tianyu on 17/5/26.
 * You are given two non-empty linked lists representing two non-negative integers.
 * The digits are stored in reverse order and each of their nodes contain a single digit.
 * Add the two numbers and return it as a linked list.
 *
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 *
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
 * Output: 7 -> 0 -> 8
 */
public class AddTwoNumbers {
    /**
     * Definition for singly-linked list.
     * public class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode(int x) { val = x; }
     * }
     */
    @Getter
    @Setter
    public static class ListNode{
        int val;
        ListNode next;
        ListNode(int x) {
            this.val = x;
        }

        private void print() {
            ListNode cur = this;
            while (cur != null) {
                System.out.println(cur.val);
                cur = cur.getNext();
            }
        }
    }
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode prev = new ListNode(0);
        ListNode head = prev;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) {
            ListNode cur = new ListNode(0);
            int sum = ((l2 == null) ? 0 : l2.val) + ((l1 == null) ? 0 : l1.val) + carry;
            cur.val = sum % 10;
            carry = sum / 10;
            prev.next = cur;
            prev = cur;

            l1 = (l1 == null) ? null : l1.next;
            l2 = (l2 == null) ? null : l2.next;
        }
        return head.next;
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(3);
        ListNode ln11 = new ListNode(8);
        l1.setNext(ln11);
        ListNode ln12 = new ListNode(4);
        ln11.setNext(ln12);
        ListNode ln13 = new ListNode(8);
        ln12.setNext(ln13);

        ListNode l2 = new ListNode(2);
        ListNode ln21 = new ListNode(5);
        l2.setNext(ln21);
        ListNode ln22 = new ListNode(1);
        ln21.setNext(ln22);
        ListNode ln23 = new ListNode(4);
        ln22.setNext(ln23);

        ListNode l3 = addTwoNumbers(l1,l2);

        l3.print();
    }
}
