package com.mirumagency.uhc.premember.core.services.repository.jcr;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.employer.EmployerCopy;
import com.mirumagency.uhc.premember.core.services.repository.EmployerRepo;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.EmployerDataConfigPath;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.EmployerPath;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.FooterConfigPath;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.LogoMetadataPath;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResourceResolver;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.ResourceGentleman;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = EmployerRepo.class)
public class EmployerRepoJcr implements EmployerRepo {

  @Reference
  private ResourceGentleman resourceGentleman;

  @Reference
  private ContentRepoJcr contentRepo;

  @Override
  public Employer load(String resourcePath) {
    return resourceGentleman.withResolver(resolver -> {
      EmployerPath employerPath = EmployerPath.of(resourcePath);
      EmployerCopy copy = contentRepo.loadEmployerCopy(employerPath);
      NiceResource employerResource = getEmployerResource(resolver, employerPath);
      NiceResource employerDataConfigResource = getEmployerDataConfigResource(resolver,
          employerPath);
      NiceResource footerConfigResource = getFooterConfigResource(resolver, employerPath);

      return Employer
          .of(employerPath, employerResource, employerDataConfigResource, footerConfigResource, copy,
              contentRepo.loadData(employerPath.dataConfigPath().path(), employerPath),
              contentRepo.loadData(LogoMetadataPath.of(employerDataConfigResource).path(), employerPath));
    });
  }

  private NiceResource getEmployerResource(NiceResourceResolver resolver, EmployerPath employerPath) {
    return resolver.getResourceOrThrow(employerPath.path());
  }

  private NiceResource getEmployerDataConfigResource(NiceResourceResolver resolver,
      EmployerPath employerPath) {
    EmployerDataConfigPath employerDataConfigPath = employerPath.dataConfigPath();
    return resolver.getResourceOrThrow(employerDataConfigPath.path());
  }

  private NiceResource getFooterConfigResource(NiceResourceResolver resolver,
      EmployerPath employerPath) {
    FooterConfigPath footerConfigPath = employerPath.footerConfigPath();
    return resolver.getResourceOrThrow(footerConfigPath.path());
  }
}
