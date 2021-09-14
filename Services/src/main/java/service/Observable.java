package service;

import service.Observer;

public interface Observable
{
    void add_observer(Observer o);
    void notify_observer();
    void remove_observers();
    void remove_observer(Observer o);
}
