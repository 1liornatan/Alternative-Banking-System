package manager.categories;

import java.util.Set;

public class CategoriesDTO {
    Set<String> categories;

    public CategoriesDTO(Set<String> categories) {
        this.categories = categories;
    }

    public Set<String> getCategories() {
        return categories;
    }
}
