package manager.categories;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoriesDTO {
    List<String> categories;

    public CategoriesDTO(Set<String> categories) {
        this.categories = new ArrayList<>();

        for(String str : categories)
            this.categories.add(str);
    }

    public List<String> getCategories() {
        return categories;
    }
}
