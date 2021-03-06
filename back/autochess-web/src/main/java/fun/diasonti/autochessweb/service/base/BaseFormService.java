package fun.diasonti.autochessweb.service.base;

import fun.diasonti.autochessweb.data.entity.base.BaseEntity;
import fun.diasonti.autochessweb.data.form.base.BaseForm;
import fun.diasonti.autochessweb.data.mappers.base.BaseMapper;
import fun.diasonti.autochessweb.repository.base.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseFormService<E extends BaseEntity, F extends BaseForm> {

    protected abstract BaseRepository<E> getRepository();
    protected abstract BaseMapper<E, F> getMapper();

    @Transactional(readOnly = true)
    public F get(long id) {
        return getRepository().findById(id).map(this::entityToForm).orElse(null);
    }

    @Transactional
    public E save(F form) {
        return getRepository().save(formToEntity(form));
    }

    protected E formToEntity(F form) {
        return getMapper().formToEntity(form);
    }

    protected F entityToForm(E entity) {
        return getMapper().entityToForm(entity);
    }

}
