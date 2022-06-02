package manager.categories;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoriesDTO {
    final List<String> categories;

    public CategoriesDTO(Set<String> categories) {
        this.categories = new ArrayList<>();

        this.categories.addAll(categories);
    }

    public List<String> getCategories() {
        return categories;
    }
}
