package by.project.library.springweblibrary.jsfui.controller;

import by.project.library.springweblibrary.dao.PublisherDao;
import by.project.library.springweblibrary.domain.Publisher;
import by.project.library.springweblibrary.jsfui.model.LazyDataTable;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import java.util.List;

@ManagedBean
@SessionScoped
@Component
@Getter
@Setter
@Transactional
public class PublisherController extends AbstractController<Publisher> {

    private int rowsCount = 20;

    private int first;

    private Page<Publisher> publisherPages;

    @Autowired
    private PublisherDao publisherDao;


    private Publisher selectedPublisher;


    private LazyDataTable<Publisher> lazyModel;

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataTable(this);
    }

    public void save() {
        publisherDao.save(selectedPublisher);
        RequestContext.getCurrentInstance().execute("PF('dialogPublisher').hide()");
        //PrimeFaces.current().executeScript("PF('dialogPublisher').hide()");
    }

    @Override
    public Page<Publisher> search(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection) {
        return publisherPages;
    }

    @Override
    public void addAction() {
        selectedPublisher = new Publisher();
        showEditDialog();
    }


    @Override
    public void editAction() {
        showEditDialog();
    }

    @Override
    public void deleteAction() {
        publisherDao.delete(selectedPublisher);
    }

    private void showEditDialog() {
        RequestContext.getCurrentInstance().execute("PF('dialogPublisher').show()");
        //PrimeFaces.current().executeScript("PF('dialogPublisher').show()");
    }

    public List<Publisher> find(String name) {
        return publisherDao.search(name);
    }
}
