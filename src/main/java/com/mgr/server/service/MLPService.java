package com.mgr.server.service;

import com.mgr.server.entity.Memory;
import com.mgr.server.enums.Level;
import com.mgr.server.exceptions.NotFoundHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class MLPService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbit.input.queue.name}")
    private String rabbitInputName;

    @Value("${rabbit.output.queue.name}")
    private String rabbitOutputName;

    @Value("execute.time")
    private String times;

    @RabbitListener(queues = "${rabbit.input.queue.name}")
    public void rabbitReader(Memory _memory) throws InterruptedException {
        executeLogic(_memory);
    }

    private void executeLogic(Memory _memory)  {
        Runnable runnable = () -> {
            while (!_memory.getReady() && _memory.getPercent() != 100.0) {
                _memory.setPercent(_memory.getPercent() + 0.5);
//                primeNumbersTill(Level.LOW);
                try {
                    Thread.sleep(Long.parseLong(times));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rabbitTemplate.convertAndSend(rabbitOutputName,_memory);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static boolean isPrimeBruteForce(int number) {
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static List<Integer> primeNumbersTill(Level _level) {
        int _number;
        if (_level == null)
            throw new NotFoundHandler(" level");
        switch (_level) {
            case LOW:
                _number = 10000;
                break;
            case MEDIUM:
                _number = 50000;
                break;
            case HIGH:
                _number = 100000;
                break;
            default:
                _number = 1;
        }
        return primeNumbersBruteForce(_number);
    }

    private static List<Integer> primeNumbersBruteForce(int n) {
        List<Integer> primeNumbers = new LinkedList<>();
        for (int i = 2; i <= n; i++) {
            if (isPrimeBruteForce(i)) {
                primeNumbers.add(i);
            }
        }
        return primeNumbers;
    }


}
