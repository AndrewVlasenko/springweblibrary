package by.project.library.springweblibrary.jsfui.controller;

import by.project.library.springweblibrary.dao.BookDao;
import by.project.library.springweblibrary.dao.GenreDao;
import by.project.library.springweblibrary.domain.Book;
import by.project.library.springweblibrary.jsfui.enums.SearchType;
import by.project.library.springweblibrary.jsfui.model.LazyDataTable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.ResourceBundle;

@ManagedBean
@SessionScoped
@Component
@Getter
@Setter
@Log
@Transactional
public class BookController extends AbstractController<Book> {

    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final int TOP_BOOKS_LIMIT = 5;

    private int rowsCount = DEFAULT_PAGE_SIZE;

    private SearchType searchType;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private GenreDao genreDao;

    private LazyDataTable<Book> lazyModel;

    private Page<Book> bookPages;

    private List<Book> topBooks;

    private String searchText;

    private long selectedGenreId;

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataTable(this);
    }

    @Override
    public Page<Book> search(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection) {
        if (sortField == null){
            sortField = "name";
        }

        if (searchType == null){
            bookPages = bookDao.getAll(pageNumber,pageSize, sortField, sortDirection);
        } else {
            switch (searchType){
                case SEARCH_GENRE:
                    bookPages = bookDao.findByGenre(pageNumber, pageSize, sortField, sortDirection, selectedGenreId);
                    break;
                case SEARCH_TEXT:
                    bookPages = bookDao.search(pageNumber, pageSize, sortField, sortDirection, searchText);
                    break;
                case ALL:
                    bookPages = bookDao.getAll(pageNumber, pageSize, sortField, sortDirection);
                    break;
            }
        }
        return bookPages;
    }

    public List<Book> getTopBooks() {
        topBooks = bookDao.findTopBooks(TOP_BOOKS_LIMIT);
        return topBooks;
    }

    public void showBooksByGenre(long selectedGenreId){
        searchType = SearchType.SEARCH_GENRE;
        this.selectedGenreId = selectedGenreId;
    }

    public void showAll(){
        searchType = SearchType.ALL;
    }


    public String getSearchMessage(){

        ResourceBundle bundle = ResourceBundle.getBundle("library", FacesContext.getCurrentInstance().getViewRoot().getLocale());


        String message = null;

        if (searchType == null){
            return null;
        }
        switch (searchType) {
            case SEARCH_GENRE:
                message = bundle.getString("genre") + ": '" + genreDao.get(selectedGenreId) + "'";
                break;
            case SEARCH_TEXT:

                if (searchText==null || searchText.trim().length() == 0){
                    return null;
                }

                message = bundle.getString("search") + ": '" + searchText + "'";
                break;
        }

        return message;
    }

    public void searchAction(){
        searchText = searchText.trim();
        searchType = SearchType.SEARCH_TEXT;
    }
}
