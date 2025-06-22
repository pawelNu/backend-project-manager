package com.pawelnu.projectmanager.utils;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageableParams {
  private String sortField;
  private String sortDir;
  private int offset;
  private int limit;
  private Map<String, String> filters;
}
